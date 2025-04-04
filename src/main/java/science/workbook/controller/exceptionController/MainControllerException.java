package science.workbook.controller.exceptionController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import science.workbook.dto.api.Api;
import science.workbook.exception.repository.NotFoundUserByEmail;
import science.workbook.exception.repository.NotFoundUserById;
import science.workbook.exception.service.mail.MailSendException;
import science.workbook.exception.service.user.NotMatchPassword;
import science.workbook.exception.token.InvalidRefreshTokenException;

import static science.workbook.exception.constant.ApiErrorMessage.비밀번호에러;
import static science.workbook.exception.constant.ApiErrorMessage.유저찾기실패;
import static science.workbook.exception.constant.ApiErrorMessage.이메일_전송_에러;
import static science.workbook.exception.constant.ApiErrorMessage.재발급_토큰_에러;

@Slf4j
@RestControllerAdvice(basePackages = "science.workbook.controller")
public class MainControllerException {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundUserByEmail.class)
    public Api<String> userNotMatchEmail(NotFoundUserByEmail e) {
        log.error("[NotFoundUserByEmail] ex : {}", e.getMessage());
        return new Api<>(유저찾기실패);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotMatchPassword.class)
    public Api<String> userNotMatchPassword(NotMatchPassword e) {
        log.error("[NotMatchPassword] ex : {}", e.getMessage());
        return new Api<>(비밀번호에러);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundUserById.class)
    public Api<String> userNotMatchEmail(NotFoundUserById e) {
        log.error("[NotFoundUserById] ex : {}", e.getMessage());
        return new Api<>(유저찾기실패);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public Api<String> notMatchRefreshToken(InvalidRefreshTokenException e) {
        log.error("[InvalidRefreshTokenException] ex : {}", e.getMessage());
        return new Api<>(재발급_토큰_에러);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MailSendException.class)
    public Api<String> failSendEmail(MailSendException e) {
        log.error("[MailSendException] ex : {}", e.getMessage());
        return new Api<>(이메일_전송_에러);
    }
}
