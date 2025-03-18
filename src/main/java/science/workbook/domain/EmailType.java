package science.workbook.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;

import static science.workbook.util.UserUtil.generateRandomCode;

@Getter
@Document
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailType {
    @Id
    private BigInteger id;
    private String email;
    private String code;
    private Boolean valid;

    @Indexed
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public EmailType(String email) {
        this.email = email;
        this.code = generateRandomCode(6);
        this.valid = Boolean.FALSE;
    }

    public Boolean checkEmail(String checkCode) {
        if(Objects.equals(this.code, checkCode)) {
            this.valid = Boolean.TRUE;
        }
        return this.valid;
    }
}
