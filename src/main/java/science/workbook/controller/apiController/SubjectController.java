package science.workbook.controller.apiController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import science.workbook.domain.Gradle;
import science.workbook.domain.Subject;
import science.workbook.dto.api.Api;
import science.workbook.dto.api.ApiMessage;
import science.workbook.dto.request.CreateSubjectDto;
import science.workbook.dto.request.DeleteSubjectDto;
import science.workbook.dto.response.SubjectsDto;
import science.workbook.exception.repository.ExistGradle;
import science.workbook.exception.repository.NotFoundSubjectByName;
import science.workbook.service.GradleService;
import science.workbook.service.SubjectService;

import java.util.List;

import static science.workbook.dto.api.ApiServerMessage.과목_리스트;
import static science.workbook.dto.api.ApiServerMessage.과목_삭제_성공;
import static science.workbook.dto.api.ApiServerMessage.과목_추가_성공;
import static science.workbook.exception.constant.ApiErrorMessage.과목_없음_에러;
import static science.workbook.exception.constant.ApiErrorMessage.과목_존재_에러;

@Slf4j
@RestController("/subject")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;
    private final GradleService gradleService;

    @GetMapping("/user")
    public Api<SubjectsDto> findAllSubject() {
        List<Subject> allSubjects = subjectService.findAllSubjects();
        SubjectsDto dto = new SubjectsDto(allSubjects);
        return new Api<> (dto, 과목_리스트);
    }

    @PostMapping("/manager")
    public Api<ApiMessage> createSubject(@Validated @RequestBody CreateSubjectDto dto) {
        if(!subjectService.validSubjectName(dto.getSubjectName())) {
            subjectService.createSubject(dto.getSubjectName());
            return new Api<>(과목_추가_성공);
        }

        throw new ExistGradle(과목_존재_에러);
    }

    @DeleteMapping("/manager")
    public Api<ApiMessage> deleteSubject(@Validated @RequestBody DeleteSubjectDto dto) {
        if(subjectService.validSubjectName(dto.getSubjectName())) {
            Subject subject = subjectService.findSubject(dto.getSubjectName());
            List<Gradle> gradles = gradleService.findAllGradle(subject);

            gradleService.deleteGradles(gradles);
            subjectService.deleteSubject(dto.getSubjectName());

            return new Api<>(과목_삭제_성공);
        }
        throw new NotFoundSubjectByName(과목_없음_에러);
    }
}
