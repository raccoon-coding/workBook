package science.workbook.exception.repository;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class NotFoundSubjectByName extends CustomException {
    public NotFoundSubjectByName(ApiMessage message) {
        super(message);
    }
}
