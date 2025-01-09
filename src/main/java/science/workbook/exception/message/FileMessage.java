package science.workbook.exception.message;

import science.workbook.dto.api.ApiMessage;

public enum FileMessage implements ApiMessage {
    PDF_저장_실패("PDF 저장 실패하였습니다", 500);

    FileMessage(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    private final String message;
    private final Integer code;

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
