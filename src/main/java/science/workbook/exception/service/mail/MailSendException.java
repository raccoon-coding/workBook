package science.workbook.exception.service.mail;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class MailSendException extends CustomException {
    public MailSendException(ApiMessage message) {
        super(message);
    }
}
