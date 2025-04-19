package science.workbook.controller.apiController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import science.workbook.domain.Gradle;
import science.workbook.domain.Subject;
import science.workbook.dto.api.Api;
import science.workbook.dto.api.ApiMessage;
import science.workbook.dto.request.CreateGradleDto;
import science.workbook.dto.request.CreateSubjectDto;
import science.workbook.dto.request.DeleteGradleDto;
import science.workbook.dto.request.DeleteSubjectDto;
import science.workbook.exception.repository.ExistGradle;
import science.workbook.exception.repository.NotFoundSubjectByName;
import science.workbook.service.GradleService;
import science.workbook.service.SubjectService;

import java.util.List;

import static science.workbook.dto.api.ApiServerMessage.과목_삭제_성공;
import static science.workbook.dto.api.ApiServerMessage.과목_추가_성공;
import static science.workbook.dto.api.ApiServerMessage.목차_삭제_성공;
import static science.workbook.dto.api.ApiServerMessage.목차_생성_성공;
import static science.workbook.exception.constant.ApiErrorMessage.과목_없음_에러;
import static science.workbook.exception.constant.ApiErrorMessage.과목_존재_에러;

@Slf4j
@RestController("/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final GradleService gradleService;
    private final SubjectService subjectService;

    @PostMapping("/gradle")
    public Api<ApiMessage> createGradle(@Validated @RequestBody CreateGradleDto dto) {
        Subject subject = subjectService.findSubject(dto.getSubjectName());
        gradleService.addGradle(dto.getGradleName(), subject);
        return new Api<>(목차_생성_성공);
    }

    @DeleteMapping("/gradle")
    public Api<ApiMessage> deleteGradle(@Validated @RequestBody DeleteGradleDto dto) {
        gradleService.deleteGradle(dto.getGradleName());
        return new Api<>(목차_삭제_성공);
    }

    @PostMapping("/subject")
    public Api<ApiMessage> createSubject(@Validated @RequestBody CreateSubjectDto dto) {
        if(!subjectService.validSubjectName(dto.getSubjectName())) {
            subjectService.createSubject(dto.getSubjectName());
            return new Api<>(과목_추가_성공);
        }

        throw new ExistGradle(과목_존재_에러);
    }

    @DeleteMapping("/subject")
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
