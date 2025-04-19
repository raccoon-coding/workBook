package science.workbook.exception.repository;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class ExistGradle extends CustomException {
    public ExistGradle(ApiMessage message) {
        super(message);
    }
}
