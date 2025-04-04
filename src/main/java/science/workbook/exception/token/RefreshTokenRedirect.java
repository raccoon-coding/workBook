package science.workbook.exception.token;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class RefreshTokenRedirect extends CustomException {
    public RefreshTokenRedirect(ApiMessage message) {
        super(message);
    }
}
