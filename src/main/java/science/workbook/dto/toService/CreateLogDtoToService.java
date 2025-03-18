package science.workbook.dto.toService;

public record CreateLogDtoToService(String userId, String url, String body, String errorMessage) {
    public static CreateLogDtoToService ofSuccessLog(String url, String body) {
        return new CreateLogDtoToService(null, url, body, null);
    }

    public static CreateLogDtoToService ofExceptionLog(String url, String body, String errorMessage) {
        return new CreateLogDtoToService(null, url, body, errorMessage);
    }
}
