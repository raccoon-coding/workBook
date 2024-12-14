package science.workbook.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Getter
@Document(collection = "problem")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Problem extends DateTime {
    @Id
    private BigInteger id;
    private Integer problemNumber;
    private Long problemTypeId;
    private Boolean wrongProblemCheck;

    public Problem(Integer problemNumber, Long problemTypeId) {
        this.problemNumber = problemNumber;
        this.problemTypeId = problemTypeId;
        this.wrongProblemCheck = Boolean.TRUE;
    }

    public void checkWrongProblem() {
        this.wrongProblemCheck = Boolean.FALSE;
    }
}
