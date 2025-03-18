package science.workbook.exception.domain;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class NotEqualEmailValidCode extends CustomException {
    public NotEqualEmailValidCode(ApiMessage message) {
        super(message);
    }
}
