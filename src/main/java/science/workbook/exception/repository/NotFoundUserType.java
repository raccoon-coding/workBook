package science.workbook.exception.repository;

public class NotFoundUserType extends RuntimeException {
    public NotFoundUserType(String message) {
        super(message);
    }
}
