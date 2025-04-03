package science.workbook.controller.exceptionController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import science.workbook.dto.api.Api;
import science.workbook.exception.repository.NotFoundUserByEmail;
import science.workbook.exception.repository.NotFoundUserById;

import static science.workbook.exception.constant.ApiErrorMessage.비밀번호에러;
import static science.workbook.exception.constant.ApiErrorMessage.유저찾기실패;

@Slf4j
@RestControllerAdvice(basePackages = "science.workbook.controller")
public class MainControllerException {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundUserByEmail.class)
    public Api<String> userNotMatchEmail(NotFoundUserByEmail e) {
        log.error("[NotFoundUserByEmail] ex : {}", e.getMessage());
        return new Api<>(유저찾기실패);
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(NotFoundUserByEmail.class)
//    public Api<String> userNotMatchPassword(NotFoundUserByEmail e) {
//        log.error("[NotMatchPassword] ex : {}", e.getMessage());
//        return new Api<>(비밀번호에러);
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundUserById.class)
    public Api<String> userNotMatchEmail(NotFoundUserById e) {
        log.error("[NotFoundUserById] ex : {}", e.getMessage());
        return new Api<>(비밀번호에러);
    }
}
