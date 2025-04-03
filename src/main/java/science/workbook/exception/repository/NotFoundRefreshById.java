package science.workbook.exception.repository;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class NotFoundRefreshById extends CustomException {
    public NotFoundRefreshById(ApiMessage message) {
        super(message);
    }
}
