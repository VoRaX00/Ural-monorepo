package ru.ural.ai.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import ru.ural.ai.dto.FileDto;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileServiceClient {

    private static final ParameterizedTypeReference<List<FileDto>> FILE_LIST_TYPE = new ParameterizedTypeReference<>() {
    };

    @Qualifier("fileServiceRestClient")
    private final RestClient fileServiceRestClient;

    public List<FileDto> getFiles(List<Long> ids, String authorization) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }

        return fileServiceRestClient.get()
                .uri(uriBuilder -> buildGetFilesUri(uriBuilder, ids))
                .headers(headers -> addAuthorizationHeader(headers, authorization))
                .retrieve()
                .body(FILE_LIST_TYPE);
    }

    private URI buildGetFilesUri(UriBuilder uriBuilder, List<Long> ids) {
        UriBuilder builder = uriBuilder.path("/api/files");
        ids.forEach(id -> builder.queryParam("ids", id));
        return builder.build();
    }

    private void addAuthorizationHeader(HttpHeaders headers, String authorization) {
        if (authorization != null && !authorization.isBlank()) {
            headers.set(HttpHeaders.AUTHORIZATION, authorization);
        }
    }

}
