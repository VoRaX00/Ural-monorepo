package ru.ural.ai.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ural.ai.clients.FileServiceClient;
import ru.ural.ai.clients.GeminiCarPhotoAnalysisClient;
import ru.ural.ai.clients.ImageDownloadClient;
import ru.ural.ai.dto.FileDto;
import ru.ural.ai.events.CarPhotoAnalysisCompletedEvent;
import ru.ural.ai.events.CarPhotoAnalysisRequestedEvent;
import ru.ural.ai.models.CarPhotoAnalysisResult;
import ru.ural.ai.models.DownloadedImage;
import ru.ural.ai.producers.CarPhotoAnalysisResultProducer;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarPhotoAnalysisService {

    private static final String UNKNOWN_STATUS = "UNKNOWN";

    private final FileServiceClient fileServiceClient;

    private final ImageDownloadClient imageDownloadClient;

    private final GeminiCarPhotoAnalysisClient geminiCarPhotoAnalysisClient;

    private final CarPhotoAnalysisResultProducer resultProducer;

    public void analyze(CarPhotoAnalysisRequestedEvent event) {
        if (event.getFileIds() == null || event.getFileIds().isEmpty()) {
            sendResult(event.getCarId(), "Фотографии для анализа не переданы", UNKNOWN_STATUS, false);
            return;
        }

        try {
            List<String> imageUrls = fileServiceClient.getFiles(event.getFileIds(), event.getAuthorization()).stream()
                    .map(FileDto::getUrl)
                    .filter(Objects::nonNull)
                    .filter(url -> !url.isBlank())
                    .toList();
            if (imageUrls.isEmpty()) {
                sendResult(event.getCarId(), "Файловый сервис не вернул ссылки на фотографии", UNKNOWN_STATUS, false);
                return;
            }

            List<DownloadedImage> images = imageUrls.stream()
                    .map(imageDownloadClient::download)
                    .toList();

            CarPhotoAnalysisResult analysis = geminiCarPhotoAnalysisClient.analyze(
                    images,
                    event.getCarInfo(),
                    event.getAutocodeInfo()
            );
            sendResult(event.getCarId(), analysis.getSummary(), analysis.getStatus(), true);
        } catch (RuntimeException exception) {
            log.error("Failed to analyze car photos for car id: {}", event.getCarId(), exception);
            sendResult(event.getCarId(), "Не удалось выполнить анализ фотографий", UNKNOWN_STATUS, false);
        }
    }

    private void sendResult(Long carId, String summary, String status, boolean success) {
        resultProducer.send(CarPhotoAnalysisCompletedEvent.builder()
                .carId(carId)
                .summary(summary)
                .status(status)
                .success(success)
                .build());
    }

}
