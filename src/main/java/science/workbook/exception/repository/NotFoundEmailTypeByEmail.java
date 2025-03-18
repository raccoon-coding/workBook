package science.workbook.exception.repository;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class NotFoundEmailTypeByEmail extends CustomException {
    public NotFoundEmailTypeByEmail(ApiMessage message) {
        super(message);
    }
}
