package science.workbook.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import science.workbook.dto.toController.CustomMultipartFile;

import java.io.ByteArrayOutputStream;

@Service
public class PdfTransferService {
    public byte[] generatePdfFromText(String content) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph(content));
            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF 생성 실패", e);
        }
    }

    public MultipartFile transferPdf(byte[] content) {
        return new CustomMultipartFile(content, "file", "testFileName", "application/pdf");
    }
}
