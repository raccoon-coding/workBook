package science.workbook.exception.service.file;

public class NotExistDirectory extends RuntimeException {
    public NotExistDirectory(String message) {
        super(message);
    }
}
