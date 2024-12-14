package science.workbook.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import science.workbook.dto.toService.CreateLogDto;

import java.math.BigInteger;

@Getter
@Document(collection = "log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Log {
    @Id
    private BigInteger id;
    private String userId;
    private String logData;
    private String requestUrl;
    private String errorMessage;

    public Log(CreateLogDto dto) {
        this.userId = dto.userId();
        this.logData = dto.body();
        this.requestUrl = dto.url();
        this.errorMessage = dto.errorMessage();
    }
}
