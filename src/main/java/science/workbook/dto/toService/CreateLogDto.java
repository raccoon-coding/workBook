package science.workbook.dto.toService;

public record CreateLogDto(String userId, String url, String body, String errorMessage) {
    public static CreateLogDto ofSuccessLog(String url, String body) {
        return new CreateLogDto(null, url, body, null);
    }

    public static CreateLogDto ofExceptionLog(String url, String body, String errorMessage) {
        return new CreateLogDto(null, url, body, errorMessage);
    }
}
