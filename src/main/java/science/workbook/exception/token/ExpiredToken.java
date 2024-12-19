package science.workbook.exception.token;

public class ExpiredToken extends RuntimeException {
    public ExpiredToken(String message) {
        super(message);
    }
}
