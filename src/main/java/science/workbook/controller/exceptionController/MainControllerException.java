package science.workbook.controller.exceptionController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "science.workbook.controller")
public class MainControllerException {
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(NotEqualOldPassword.class)
//    public API<String> userNotMatchOldPassword(NotEqualOldPassword e) {
//        log.error("[NotEqualOldPassword] ex : {}", e.getMessage());
//        return new API<>(APIErrorMessage.비밀번호_변경_에러);
//    }
}
