package science.workbook.exception.service.file;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class FailSavePDF extends CustomException {
    public FailSavePDF(ApiMessage apiMessage) {
        super(apiMessage);
    }
}
