package science.workbook.dto.response;

import lombok.Getter;
import science.workbook.domain.Gradle;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GradleDto {
    private List<String> gradle;

    public GradleDto(List<Gradle> gradle) {
        this.gradle = gradle.stream()
                .map(Gradle::getGradleName)
                .collect(Collectors.toList());
    }
}
