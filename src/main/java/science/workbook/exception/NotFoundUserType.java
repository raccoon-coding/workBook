package science.workbook.exception;

public class NotFoundUserType extends RuntimeException {
    public NotFoundUserType(String message) {
        super(message);
    }
}
