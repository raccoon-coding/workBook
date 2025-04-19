package science.workbook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import science.workbook.dto.request.gpt.OpenAIResponse;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAIClient {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String apiKey;

    public Mono<String> callOpenAI(Map<String, Object> requestBody) {
        return webClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }

    public OpenAIResponse parseResponse(String json) {
        try {
            return objectMapper.readValue(json, OpenAIResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("파싱 실패", e);
        }
    }
}
