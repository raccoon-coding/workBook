package science.workbook.dto.toDomain;

import science.workbook.domain.Gradle;
import science.workbook.domain.Subject;

public record ProblemTypeDtoToDomain(Gradle gradle, Subject subject, String problemType) {
}
