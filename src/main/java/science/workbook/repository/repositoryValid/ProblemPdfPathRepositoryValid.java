package science.workbook.repository.repositoryValid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import science.workbook.domain.ProblemPdfPath;
import science.workbook.repository.repositoryMongo.ProblemPdfPathRepository;

@Repository
@RequiredArgsConstructor
public class ProblemPdfPathRepositoryValid {
    private final ProblemPdfPathRepository repository;

    public void savePdf(ProblemPdfPath pdfPath) {
        repository.save(pdfPath);
    }

    public void deletePdf(ProblemPdfPath pdfPath) {
        repository.delete(pdfPath);
    }
}
