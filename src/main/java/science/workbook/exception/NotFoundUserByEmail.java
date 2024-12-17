package science.workbook.exception;

public class NotFoundUserByEmail extends RuntimeException {
    public NotFoundUserByEmail(String message) {
        super(message);
    }
}
