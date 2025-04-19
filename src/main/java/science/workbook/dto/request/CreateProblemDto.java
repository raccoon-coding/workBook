package science.workbook.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProblemDto {
    private String subjectName;
    private String gradleStartName;
    private String gradleEndName;
}
