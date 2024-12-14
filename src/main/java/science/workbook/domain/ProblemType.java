package science.workbook.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import science.workbook.dto.toDomain.ProblemTypeSignatureDto;

import java.math.BigInteger;

@Getter
@Document(collection = "problem_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemType extends DateTime {
    @Id
    private BigInteger id;
    private Gradle gradle;
    private Subject subject;
    private String problemType;

    public ProblemType(ProblemTypeSignatureDto dto) {
        this.gradle = dto.gradle();
        this.subject = dto.subject();
        this.problemType = dto.problemType();
    }
}
