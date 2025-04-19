package science.workbook.exception.repository;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class NotFoundGradleByName extends CustomException {
    public NotFoundGradleByName(ApiMessage message) {
        super(message);
    }
}
