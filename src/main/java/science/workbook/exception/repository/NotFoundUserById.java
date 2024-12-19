package science.workbook.exception.repository;

public class NotFoundUserById extends RuntimeException {
    public NotFoundUserById(String message) {
        super(message);
    }
}
