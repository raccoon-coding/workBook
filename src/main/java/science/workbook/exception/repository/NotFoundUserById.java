package science.workbook.exception.repository;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class NotFoundUserById extends CustomException {
    public NotFoundUserById(ApiMessage message) {
        super(message);
    }
}
