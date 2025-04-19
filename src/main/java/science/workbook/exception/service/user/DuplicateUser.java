package science.workbook.exception.service.user;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class DuplicateUser extends CustomException {
    public DuplicateUser(ApiMessage message) {
        super(message);
    }
}
