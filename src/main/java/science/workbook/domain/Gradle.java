package science.workbook.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Getter
@Document(collection = "gradle")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Gradle extends DateTime {
    @Id
    private BigInteger id;
    @DBRef(lazy = true)
    @Indexed
    private Subject subject;
    private String gradleName;

    public Gradle(String gradleName, Subject subject) {
        this.gradleName = gradleName;
        this.subject = subject;
    }
}
