package ural.ru.services.proxy;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import ural.ru.properties.proxy.ProxyProperty;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class ProxyService {

    private final RestTemplate restTemplate;

    private final ProxyProperty proxyProperty;

    public abstract ResponseEntity<?> processProxyRequest(byte[] body, HttpMethod method, HttpServletRequest request);

    protected ResponseEntity<?> sendRequest(URI uri, byte[] body, HttpMethod method, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.set(headerName, request.getHeader(headerName));
        }

        headers.remove(HttpHeaders.ACCEPT_ENCODING);
        headers.remove(HttpHeaders.HOST);

        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<?> serverResponse = restTemplate.exchange(uri, method, httpEntity, byte[].class);

            HttpHeaders responseHeaders = new HttpHeaders();
            Optional<HttpHeaders> responseServerHeaders = Optional.ofNullable(serverResponse.getHeaders());

            responseServerHeaders.map(headersResponse -> headersResponse.get(HttpHeaders.CONTENT_TYPE))
                    .ifPresent(content -> responseHeaders.put(HttpHeaders.CONTENT_TYPE, content));
            responseServerHeaders.map(headersResponse -> headersResponse.get(HttpHeaders.CONTENT_DISPOSITION))
                    .ifPresent(content -> responseHeaders.put(HttpHeaders.CONTENT_DISPOSITION, content));

            return new ResponseEntity<>(serverResponse.getBody(), responseHeaders, serverResponse.getStatusCode());

        } catch (HttpStatusCodeException e) {
            if (!e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                log.error("{} error: {}", this.getClass().getName(), e.getMessage(), e);
            }

            return new ResponseEntity<>(e.getResponseBodyAsByteArray(), e.getStatusCode());
        }
    }

    public ResponseEntity<?> upload(List<MultipartFile> files, List<String> types, HttpServletRequest request) {
        URI uri = UriComponentsBuilder.fromUriString(proxyProperty.getUrl())
                .path("/files")
                .build(true)
                .toUri();

        HttpHeaders headers = createMultipartHeaders(request);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        for (MultipartFile file : files) {
            body.add("files", createFilePart("files", file));
        }

        for (String type : types) {
            body.add("types", type);
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        return restTemplate.exchange(uri, HttpMethod.POST, requestEntity, byte[].class);
    }

    public ResponseEntity<?> uploadAvatar(MultipartFile file, String metadata, HttpServletRequest request) {
        URI uri = UriComponentsBuilder.fromUriString(proxyProperty.getUrl())
                .path("/files/avatar")
                .build(true)
                .toUri();

        HttpHeaders headers = createMultipartHeaders(request);

        HttpHeaders metadataHeaders = new HttpHeaders();
        metadataHeaders.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", createRepeatableFilePart("file", file));
        body.add("metadata", new HttpEntity<>(metadata, metadataHeaders));

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        return restTemplate.exchange(uri, HttpMethod.POST, requestEntity, byte[].class);
    }

    private HttpHeaders createMultipartHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.set(headerName, request.getHeader(headerName));
        }

        headers.remove(HttpHeaders.ACCEPT_ENCODING);
        headers.remove(HttpHeaders.CONNECTION);
        headers.remove(HttpHeaders.TRANSFER_ENCODING);
        headers.remove(HttpHeaders.HOST);
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return headers;
    }

    private HttpEntity<MultipartInputStreamFileResource> createFilePart(String name, MultipartFile file) {
        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentDispositionFormData(name, file.getOriginalFilename());
        fileHeaders.setContentType(MediaType.parseMediaType(
                Optional.ofNullable(file.getContentType()).orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE)
        ));
        return new HttpEntity<>(new MultipartInputStreamFileResource(file), fileHeaders);
    }

    private HttpEntity<ByteArrayResource> createRepeatableFilePart(String name, MultipartFile file) {
        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentDispositionFormData(name, file.getOriginalFilename());
        fileHeaders.setContentType(MediaType.parseMediaType(
                Optional.ofNullable(file.getContentType()).orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE)
        ));
        try {
            return new HttpEntity<>(new MultipartByteArrayResource(file), fileHeaders);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read multipart file bytes", e);
        }
    }

    private static class MultipartByteArrayResource extends ByteArrayResource {

        private final MultipartFile file;

        private MultipartByteArrayResource(MultipartFile file) throws IOException {
            super(file.getBytes());
            this.file = file;
        }

        @Override
        public String getFilename() {
            return file.getOriginalFilename();
        }
    }

    private static class MultipartInputStreamFileResource extends InputStreamResource {

        private final MultipartFile file;

        private MultipartInputStreamFileResource(MultipartFile file) {
            super(getInputStream(file));
            this.file = file;
        }

        @Override
        public String getFilename() {
            return file.getOriginalFilename();
        }

        @Override
        public long contentLength() {
            return file.getSize();
        }

        private static InputStream getInputStream(MultipartFile file) {
            try {
                return file.getInputStream();
            } catch (Exception e) {
                throw new RuntimeException("Failed to read multipart file stream", e);
            }
        }
    }

    protected URI buildURI(HttpServletRequest request) {
        try {
            URI base = new URI(proxyProperty.getUrl());
            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base)
                    .path(request.getRequestURI().substring(request.getContextPath().length()))
                    .query(request.getQueryString());

            return builder.build(true).toUri();
        } catch (URISyntaxException e) {
            log.error("Error with create uri for proxy on cargo-service");
            throw new RuntimeException(e);
        }
    }

}
