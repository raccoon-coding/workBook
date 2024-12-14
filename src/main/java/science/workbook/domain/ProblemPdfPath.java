package science.workbook.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "problem_pdf_path")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemPdfPath {
    @Id
    private Long id;
    private String pdfPath;

    public ProblemPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
}
