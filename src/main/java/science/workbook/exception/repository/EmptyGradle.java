package science.workbook.exception.repository;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class EmptyGradle extends CustomException {
    public EmptyGradle(ApiMessage message) {
        super(message);
    }
}
