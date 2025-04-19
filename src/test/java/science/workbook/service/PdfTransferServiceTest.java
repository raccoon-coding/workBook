package science.workbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

class PdfTransferServiceTest {
    PdfTransferService pdfTransferService = new PdfTransferService();

    @Test
    void PDF_텍스트로부터_정상적으로_생성되어야_한다() {
        String content = "GPT가 생성한 문제입니다.";

        byte[] pdf = pdfTransferService.generatePdfFromText(content);

        assertThat(pdf).isNotNull();
        assertThat(pdf.length).isGreaterThan(0);
    }

    @Test
    void PDF_바이트배열이_MultipartFile_로_정상_변환되어야_한다() throws Exception {
        String content = "GPT가 생성한 문제입니다.";
        byte[] pdfBytes = pdfTransferService.generatePdfFromText(content);

        MultipartFile multipartFile = pdfTransferService.transferPdf(pdfBytes);

        assertThat(multipartFile).isNotNull();
        assertThat(multipartFile.getOriginalFilename()).isEqualTo("testFileName");
        assertThat(multipartFile.getContentType()).isEqualTo("application/pdf");
        assertThat(multipartFile.getBytes()).isEqualTo(pdfBytes);
    }
}
