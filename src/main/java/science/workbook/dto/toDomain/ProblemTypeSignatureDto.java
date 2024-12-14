package science.workbook.dto.toDomain;

import science.workbook.domain.Gradle;
import science.workbook.domain.Subject;

public record ProblemTypeSignatureDto(Gradle gradle, Subject subject, String problemType) {
}
