package science.workbook.exception.service.file;

public class ExistDirectory extends RuntimeException {
    public ExistDirectory(String message) {
        super(message);
    }
}
