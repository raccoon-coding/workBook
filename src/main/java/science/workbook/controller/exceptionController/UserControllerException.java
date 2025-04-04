package science.workbook.controller.exceptionController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import science.workbook.dto.api.Api;
import science.workbook.exception.controller.FailDeleteUserException;

import static science.workbook.exception.constant.ApiErrorMessage.유저_삭제_에러;

@Slf4j
@RestControllerAdvice(basePackages = "science.workbook.controller")
public class UserControllerException {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FailDeleteUserException.class)
    public Api<String> failDeleteUser(FailDeleteUserException e) {
        log.error("[FailDeleteUserException] ex : {}", e.getMessage());
        return new Api<>(유저_삭제_에러);
    }
}
