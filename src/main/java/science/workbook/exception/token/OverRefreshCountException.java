package science.workbook.exception.token;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class OverRefreshCountException extends CustomException {
    public OverRefreshCountException(ApiMessage message) {
        super(message);
    }
}
