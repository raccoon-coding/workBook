package science.workbook.exception.service.user;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class NotMatchPassword extends CustomException {
    public NotMatchPassword(ApiMessage message) {
        super(message);
    }
}
