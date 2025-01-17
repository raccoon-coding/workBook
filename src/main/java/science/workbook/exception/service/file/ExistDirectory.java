package science.workbook.exception.service.file;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class ExistDirectory extends CustomException {
    public ExistDirectory(ApiMessage message) {
        super(message);
    }
}
