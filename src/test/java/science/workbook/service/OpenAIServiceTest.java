package science.workbook.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import science.workbook.dto.request.gpt.Choice;
import science.workbook.dto.request.gpt.Message;
import science.workbook.dto.request.gpt.OpenAIResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenAIServiceTest {
    @Mock
    private OpenAIClient openAIClient;

    private OpenAIService openAIService;

    @BeforeEach
    void setUp() {
        openAIService = new OpenAIService(openAIClient);
    }

    @Test
    void generateProblem_success() {
        // given
        String prompt = "수학 문제 하나 만들어줘.";
        String mockJson = """
            {
              "choices": [
                {
                  "message": {
                    "content": "2 + 2는 얼마인가요?"
                  }
                }
              ]
            }
        """;

        Message message = new Message("", "2 + 2는 얼마인가요?");
        Choice choice = new Choice(message);
        OpenAIResponse mockResponse = new OpenAIResponse(List.of(choice));

        when(openAIClient.callOpenAI(any())).thenReturn(Mono.just(mockJson));
        when(openAIClient.parseResponse(mockJson)).thenReturn(mockResponse);

        // when
        String result = openAIService.generateProblem(prompt).block();

        // then
        assertEquals("2 + 2는 얼마인가요?", result);
    }
}
