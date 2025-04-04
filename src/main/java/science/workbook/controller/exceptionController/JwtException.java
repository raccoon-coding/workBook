package science.workbook.controller.exceptionController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import science.workbook.dto.api.Api;
import science.workbook.exception.token.ExpiredToken;
import science.workbook.exception.token.InvalidRefreshTokenException;
import science.workbook.exception.token.NotHaveToken;
import science.workbook.exception.token.RefreshTokenRedirect;

import static science.workbook.exception.constant.ApiErrorMessage.다른_토큰;
import static science.workbook.exception.constant.ApiErrorMessage.토큰_요청;
import static science.workbook.exception.constant.ApiErrorMessage.토큰_재로그인;

@Slf4j
@RestControllerAdvice(basePackages = "science.workbook.config")
public class JwtException {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExpiredToken.class)
    public Api<String> expiredToken(ExpiredToken e) {
        log.error("[ExpiredToken] ex : {}", e.getMessage());
        return new Api<>(토큰_재로그인);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RefreshTokenRedirect.class)
    public Api<String> expiredToken(RefreshTokenRedirect e) {
        log.error("[RefreshTokenRedirect] ex : {}", e.getMessage());
        return new Api<>(토큰_재로그인);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public Api<String> expiredToken(InvalidRefreshTokenException e) {
        log.error("[InvalidRefreshTokenException] ex : {}", e.getMessage());
        return new Api<>(다른_토큰);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotHaveToken.class)
    public Api<String> expiredToken(NotHaveToken e) {
        log.error("[NotHaveToken] ex : {}", e.getMessage());
        return new Api<>(토큰_요청);
    }
}
