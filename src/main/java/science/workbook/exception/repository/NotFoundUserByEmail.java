package science.workbook.exception.repository;

public class NotFoundUserByEmail extends RuntimeException {
    public NotFoundUserByEmail(String message) {
        super(message);
    }
}
