package science.workbook.exception.repository;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class NotFoundUserByEmail extends CustomException {
    public NotFoundUserByEmail(ApiMessage message) {
        super(message);
    }
}
