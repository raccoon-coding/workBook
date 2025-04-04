package science.workbook.exception.token;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class ExpiredToken extends CustomException {
    public ExpiredToken(ApiMessage message) {
        super(message);
    }
}
