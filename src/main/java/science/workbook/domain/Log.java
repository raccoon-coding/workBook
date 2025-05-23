package science.workbook.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import science.workbook.dto.toService.CreateLogDtoToService;

import java.math.BigInteger;

@Getter
@Document(collection = "log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Log extends DateTime {
    @Id
    @Indexed
    private BigInteger id;
    private String userId;
    private String logData;
    private String requestUrl;
    private String message;

    public Log(CreateLogDtoToService dto) {
        this.userId = dto.userId();
        this.logData = dto.body();
        this.requestUrl = dto.url();
        this.message = dto.message();
    }
}
