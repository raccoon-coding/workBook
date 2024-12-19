package science.workbook.exception.token;

public class OverRefreshCountException extends RuntimeException {
    public OverRefreshCountException(String message) {
        super(message);
    }
}
