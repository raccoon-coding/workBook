package science.workbook.exception.repository;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class NotFoundRefreshByEmail extends CustomException {
    public NotFoundRefreshByEmail(ApiMessage message) {
        super(message);
    }
}
