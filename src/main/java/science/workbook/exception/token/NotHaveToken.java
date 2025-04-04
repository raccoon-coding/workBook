package science.workbook.exception.token;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class NotHaveToken extends CustomException {
    public NotHaveToken(ApiMessage message) {
        super(message);
    }
}
