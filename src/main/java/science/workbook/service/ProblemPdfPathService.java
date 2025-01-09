package science.workbook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import science.workbook.domain.ProblemPdfPath;
import science.workbook.repository.repositoryValid.ProblemPdfPathRepositoryValid;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProblemPdfPathService {
    private final ProblemPdfPathRepositoryValid repository;

    @Transactional
    public void savePdf(String pdfPath) {
        ProblemPdfPath problemPdfPath = new ProblemPdfPath(pdfPath);
        repository.savePdf(problemPdfPath);
    }

    @Transactional
    public void deletePdf(ProblemPdfPath pdfPath){
        repository.deletePdf(pdfPath);
    }
}
