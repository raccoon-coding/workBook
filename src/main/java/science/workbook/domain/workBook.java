package science.workbook.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Document(collection = "workbook")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class workBook {
    @Id
    private Long id;
    private String userId;
    private ProblemPdfPath pdfPath;
    private List<ProblemType> problemTypes;
    private List<Problem> problems;
    private List<Problem> wrongProblems;
}
