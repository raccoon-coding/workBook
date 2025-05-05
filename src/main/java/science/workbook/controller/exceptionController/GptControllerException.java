package science.workbook.controller.exceptionController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import science.workbook.dto.api.Api;
import science.workbook.exception.repository.EmptyGradle;
import science.workbook.exception.repository.NotFoundGradleByName;
import science.workbook.exception.repository.NotFoundSubjectByName;
import science.workbook.exception.service.file.FailSavePDF;

import static science.workbook.exception.constant.ApiErrorMessage.PDF_저장_에러;
import static science.workbook.exception.constant.ApiErrorMessage.과목_없음_에러;
import static science.workbook.exception.constant.ApiErrorMessage.학년_없음_에러;

@Slf4j
@RestControllerAdvice(basePackages = "science.workbook.controller")
public class GptControllerException {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FailSavePDF.class)
    public Api<String> failPdf(FailSavePDF e) {
        log.error("[FailSavePDF] ex : {}", e.getMessage());
        return new Api<>(PDF_저장_에러);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundSubjectByName.class)
    public Api<String> notFoundSubject(NotFoundSubjectByName e) {
        log.error("[NotFoundSubjectByName] ex : {}", e.getMessage());
        return new Api<>(과목_없음_에러);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundGradleByName.class)
    public Api<String> notFoundGradle(NotFoundGradleByName e) {
        log.error("[NotFoundGradleByName] ex : {}", e.getMessage());
        return new Api<>(학년_없음_에러);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmptyGradle.class)
    public Api<String> emptyGradle(EmptyGradle e) {
        log.error("[EmptyGradle] ex : {}", e.getMessage());
        return new Api<>(학년_없음_에러);
    }
}
