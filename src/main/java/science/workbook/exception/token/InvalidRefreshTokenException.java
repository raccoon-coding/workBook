package science.workbook.exception.token;

import science.workbook.exception.CustomException;
import science.workbook.exception.constant.ApiErrorMessage;

public class InvalidRefreshTokenException extends CustomException {
    public InvalidRefreshTokenException(ApiErrorMessage message) {
        super(message);
    }
}
