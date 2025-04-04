package science.workbook.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Getter
@Document(collection = "refresh")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Refresh extends DateTime {
    @Id
    private BigInteger id;
    private String userName;
    private String email;
    private String token;

    public Refresh(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public void rotate(String token) {
        this.token = token;
    }
}
