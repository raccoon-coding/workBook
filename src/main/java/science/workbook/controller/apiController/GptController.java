package science.workbook.controller.apiController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import science.workbook.domain.Gradle;
import science.workbook.domain.Subject;
import science.workbook.domain.User;
import science.workbook.dto.request.CreateProblemDto;
import science.workbook.service.FileService;
import science.workbook.service.GradleService;
import science.workbook.service.OpenAIService;
import science.workbook.service.PdfTransferService;
import science.workbook.service.SubjectService;

import java.util.List;

import static science.workbook.util.UserUtil.getUser;

@Slf4j
@RestController
@RequestMapping("/user/gpt")
@RequiredArgsConstructor
public class GptController {
    private final OpenAIService openAIService;
    private final PdfTransferService pdfTransferService;
    private final FileService fileService;
    private final SubjectService subjectService;
    private final GradleService gradleService;

    @PostMapping("/generate")
    public Mono<ResponseEntity<byte[]>> generatePdf(@RequestBody CreateProblemDto dto) {
        User user = getUser();
        Subject subject = subjectService.findSubject(dto.getSubjectName());
        List<Gradle> gradles = gradleService.findGradles(subject, dto.getGradleStartName(), dto.getGradleEndName());
        String param = openAIService.createParam(subject, gradles);

        return openAIService.generateProblem(param)
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
