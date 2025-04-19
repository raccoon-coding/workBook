package science.workbook.dto.response;

import lombok.Getter;
import science.workbook.domain.Subject;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SubjectsDto {
    private final List<String> subjectList;

    public SubjectsDto(List<Subject> subjects) {
        this.subjectList = subjects.stream()
                .map(Subject::getSubjectName)
                .collect(Collectors.toList());
    }
}
