package science.workbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import science.workbook.dto.request.gpt.OpenAIResponse;

import java.util.Map;

@Slf4j @Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpenAIService {
    private final OpenAIClient openAIClient;

    public Mono<String> generateProblem(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", new Object[]{
                        Map.of("role", "system", "content", "너는 문제를 잘 만드는 선생님이야."),
                        Map.of("role", "user", "content", prompt)
                }
        );

        return openAIClient.callOpenAI(requestBody)
                .map(json -> {
                    try {
                        OpenAIResponse response = openAIClient.parseResponse(json);
                        if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                            return response.getChoices().get(0).getMessage().getContent();
                        }
                        return "문제 생성 실패";
                    } catch (Exception e) {
                        return "문제 생성 실패 (파싱 에러)";
                    }
                });
    }
}
