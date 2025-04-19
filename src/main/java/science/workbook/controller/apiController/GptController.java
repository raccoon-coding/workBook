package science.workbook.controller.apiController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import science.workbook.domain.User;
import science.workbook.service.FileService;
import science.workbook.service.OpenAIService;
import science.workbook.service.PdfTransferService;

import static science.workbook.util.UserUtil.getUser;

@Slf4j
@RestController("/user/gpt")
@RequiredArgsConstructor
public class GptController {
    private final OpenAIService openAIService;
    private final PdfTransferService pdfTransferService;
    private final FileService fileService;

    @PostMapping("/generate")
    public Mono<ResponseEntity<byte[]>> generatePdf(@RequestBody String prompt) {
        User user = getUser();

        return openAIService.generateProblem(prompt)
                .map(problemText -> {
                    byte[] pdfBytes = pdfTransferService.generatePdfFromText(problemText);
                    MultipartFile file = pdfTransferService.transferPdf(pdfBytes);
                    fileService.saveFile(file, user.getName());

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"generated_problem.pdf\"")
                            .contentType(MediaType.APPLICATION_PDF)
                            .contentLength(pdfBytes.length)
                            .body(pdfBytes);
                });
    }
}
