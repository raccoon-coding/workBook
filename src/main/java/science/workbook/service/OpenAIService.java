package science.workbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import science.workbook.domain.Gradle;
import science.workbook.domain.Subject;
import science.workbook.dto.request.gpt.OpenAIResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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

    public String createParam(Subject subject, List<Gradle> gradles) {
        StringBuilder paramBuilder = new StringBuilder();

        paramBuilder.append("너는 ")
                .append(subject.getSubjectName())
                .append(" 과목의 전문 AI 튜터야. 아래의 목차를 기반으로 대한민국 수능 스타일의 문제와 해설을 각각 만들어줘.\n\n");

        paramBuilder.append("목차:\n");
        IntStream.range(0, gradles.size())
                .mapToObj(i -> String.format("%d. %s", i + 1, gradles.get(i).getGradleName()))
                .forEach(line -> paramBuilder.append(line).append("\n"));

        paramBuilder.append("\n")
                .append("요청사항:\n")
                .append("- 아래의 목차 중 20개를 선택해서 문제를 만들어줘.\n")
                .append("- 각 목차마다 1개의 객관식(4지선다형) 문제를 만들어줘.\n")
                .append("- 문제 형식은 대한민국 수능 스타일로 구성해줘.\n")
                .append("- 각 문제에는 정답 번호와 이유를 포함한 해설을 함께 작성해줘.\n");

        return paramBuilder.toString();
    }
}
