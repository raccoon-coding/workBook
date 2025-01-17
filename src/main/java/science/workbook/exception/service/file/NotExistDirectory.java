package science.workbook.exception.service.file;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class NotExistDirectory extends CustomException {
    public NotExistDirectory(ApiMessage message) {
        super(message);
    }
}
