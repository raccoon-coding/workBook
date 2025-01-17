package science.workbook.exception.repository;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class NotFoundUserType extends CustomException {
    public NotFoundUserType(ApiMessage message) {
        super(message);
    }
}
