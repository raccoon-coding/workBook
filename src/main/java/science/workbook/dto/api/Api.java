package science.workbook.dto.api;

import lombok.Getter;

@Getter
public class Api<T> {
    private final T data;
    private final Integer code;
    private final String message;

    public Api(T data, ApiMessage apiMessage) {
        this.data = data;
        this.code = apiMessage.getCode();
        this.message = apiMessage.getMessage();
    }

    public Api(ApiMessage apiMessage) {
        this.data = null;
        this.code = apiMessage.getCode();
        this.message = apiMessage.getMessage();
    }
}
