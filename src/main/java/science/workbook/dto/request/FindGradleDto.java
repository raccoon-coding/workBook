package science.workbook.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindGradleDto {
    @NotBlank(message = "검색할 과목 이름을 입력해주세요.")
    private String subjectName;
}
