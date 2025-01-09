package science.workbook.exception;

import lombok.Getter;
import science.workbook.dto.api.ApiMessage;

@Getter
public abstract class CustomException extends RuntimeException {
  private final ApiMessage apiMessage;
  public CustomException(ApiMessage apiMessage) {
    super(apiMessage.getMessage());
    this.apiMessage = apiMessage;
  }
}
