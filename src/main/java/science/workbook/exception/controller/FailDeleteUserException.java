package science.workbook.exception.controller;

import science.workbook.dto.api.ApiMessage;
import science.workbook.exception.CustomException;

public class FailDeleteUserException extends CustomException {
    public FailDeleteUserException(ApiMessage message) {
        super(message);
    }
}
