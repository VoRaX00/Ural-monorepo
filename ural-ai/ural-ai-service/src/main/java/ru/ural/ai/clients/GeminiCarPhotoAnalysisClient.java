package ru.ural.ai.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import ru.ural.ai.configs.properties.GeminiProperties;
import ru.ural.ai.models.CarPhotoAnalysisResult;
import ru.ural.ai.models.DownloadedImage;
import ru.ural.exceptions.InternalServerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeminiCarPhotoAnalysisClient {

    private static final String UNKNOWN_STATUS = "UNKNOWN";

    private static final List<String> STATUSES = List.of(
            "EXCELLENT",
            "GOOD",
            "NEEDS_REPAIR",
            "CRITICAL",
            UNKNOWN_STATUS
    );

    private static final String PROMPT = "Проанализируй фотографии автомобиля.\n"
            + "Учитывай переданные характеристики транспорта и данные проверки Автокод как внешний контекст.\n"
            + "Если Автокод сообщает ДТП, ограничения, розыск, залог или большое количество владельцев, "
            + "упомяни это в summary.\n"
            + "Визуальное состояние оценивай по фотографиям, а юридические/исторические риски - по данным Автокод.\n"
            + "Верни краткое summary на русском языке и один статус состояния:\n"
            + "EXCELLENT - видимых проблем нет;\n"
            + "GOOD - есть небольшие следы эксплуатации;\n"
            + "NEEDS_REPAIR - есть заметные повреждения или требуется ремонт;\n"
            + "CRITICAL - есть серьезные повреждения, эксплуатация может быть опасна;\n"
            + "UNKNOWN - по фото нельзя надежно оценить состояние.\n"
            + "Не придумывай скрытые дефекты, оценивай только то, что видно на фото.";

    @Qualifier("geminiRestClient")
    private final RestClient geminiRestClient;

    private final GeminiProperties properties;

    private final ObjectMapper objectMapper;

    public CarPhotoAnalysisResult analyze(
            List<DownloadedImage> images,
            Map<String, Object> carInfo,
            Map<String, Object> autocodeInfo
    ) {
        if (CollectionUtils.isEmpty(images)) {
            return new CarPhotoAnalysisResult("Фотографии для анализа не переданы", UNKNOWN_STATUS);
        }
        if (properties.getApiKey() == null || properties.getApiKey().isBlank()) {
            throw new InternalServerException("Gemini API key is not configured");
        }

        JsonNode response;
        try {
            response = geminiRestClient.post()
                    .uri("/v1beta/models/%s:generateContent".formatted(properties.getModel()))
                    .body(buildRequestBody(images, carInfo, autocodeInfo))
                    .retrieve()
                    .body(JsonNode.class);
        } catch (RestClientException exception) {
            throw new InternalServerException("Failed to analyze car photos with Gemini", exception);
        }

        return parseResponse(response);
    }

    private Map<String, Object> buildRequestBody(
            List<DownloadedImage> images,
            Map<String, Object> carInfo,
            Map<String, Object> autocodeInfo
    ) {
        List<Map<String, Object>> parts = new ArrayList<>();
        parts.add(Map.of("text", PROMPT));
        parts.add(Map.of("text", buildContextText(carInfo, autocodeInfo)));
        images.forEach(image -> parts.add(Map.of(
                "inline_data", Map.of(
                        "mime_type", image.getMimeType(),
                        "data", image.getBase64Data()
                )
        )));

        return Map.of(
                "contents", List.of(Map.of("parts", parts)),
                "generationConfig", Map.of(
                        "responseMimeType", "application/json",
                        "responseJsonSchema", buildResponseSchema()
                )
        );
    }

    private String buildContextText(Map<String, Object> carInfo, Map<String, Object> autocodeInfo) {
        try {
            return "Характеристики транспорта:\n%s\n\nДанные Автокод:\n%s".formatted(
                    objectMapper.writeValueAsString(carInfo == null ? Map.of() : carInfo),
                    objectMapper.writeValueAsString(autocodeInfo == null ? Map.of() : autocodeInfo)
            );
        } catch (JsonProcessingException exception) {
            return "Характеристики транспорта и данные Автокод не удалось сериализовать.";
        }
    }

    private Map<String, Object> buildResponseSchema() {
        return Map.of(
                "type", "object",
                "additionalProperties", false,
                "properties", Map.of(
                        "summary", Map.of("type", "string"),
                        "status", Map.of(
                                "type", "string",
                                "enum", STATUSES
                        )
                ),
                "required", List.of("summary", "status")
        );
    }

    private CarPhotoAnalysisResult parseResponse(JsonNode response) {
        String outputText = response == null
                ? null
                : response.path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text")
                .asText(null);
        if (outputText == null || outputText.isBlank()) {
            throw new InternalServerException("Gemini returned empty car photo analysis response");
        }

        try {
            JsonNode result = objectMapper.readTree(outputText);
            return new CarPhotoAnalysisResult(
                    result.path("summary").asText(),
                    parseStatus(result.path("status").asText())
            );
        } catch (JsonProcessingException exception) {
            throw new InternalServerException("Failed to parse car photo analysis response", exception);
        }
    }

    private String parseStatus(String value) {
        if (STATUSES.contains(value)) {
            return value;
        }

        return UNKNOWN_STATUS;
    }

}
