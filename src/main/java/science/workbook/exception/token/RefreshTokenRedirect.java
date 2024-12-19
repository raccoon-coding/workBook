package science.workbook.exception.token;

public class RefreshTokenRedirect extends RuntimeException {
    public RefreshTokenRedirect(String message) {
        super(message);
    }
}
