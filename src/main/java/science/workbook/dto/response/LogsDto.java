package science.workbook.dto.response;

import lombok.Getter;
import science.workbook.domain.Log;

import java.time.LocalDateTime;

@Getter
public class LogsDto {
    private final String userId;
    private final String logData;
    private final String requestUrl;
    private final String message;
    private final LocalDateTime logTime;

    public LogsDto(Log log) {
        this.userId = log.getUserId();
        this.logData = log.getLogData();
        this.requestUrl = log.getRequestUrl();
        this.message = log.getMessage();
        this.logTime = log.getCreatedAt();
    }
}
