package ru.ural.ai.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import ru.ural.ai.models.DownloadedImage;
import ru.ural.exceptions.InternalServerException;

import java.net.URI;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class ImageDownloadClient {

    @Qualifier("imageDownloadRestClient")
    private final RestClient imageDownloadRestClient;

    public DownloadedImage download(String imageUrl) {
        try {
            ResponseEntity<byte[]> response = imageDownloadRestClient.get()
                    .uri(URI.create(imageUrl))
                    .retrieve()
                    .toEntity(byte[].class);

            byte[] body = response.getBody();
            if (body == null || body.length == 0) {
                throw new InternalServerException("Downloaded image is empty");
            }

            String contentType = resolveContentType(response.getHeaders());
            return new DownloadedImage(contentType, Base64.getEncoder().encodeToString(body));
        } catch (RestClientException exception) {
            throw new InternalServerException("Failed to download image for AI analysis", exception);
        }
    }

    private String resolveContentType(HttpHeaders headers) {
        MediaType contentType = headers.getContentType();
        if (contentType == null) {
            return MediaType.IMAGE_JPEG_VALUE;
        }

        return contentType.toString();
    }

}
