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
import science.workbook.dto.request.DeleteGradleDto;
import science.workbook.dto.request.FindGradleDto;
import science.workbook.dto.response.GradleDto;
import science.workbook.service.GradleService;
import science.workbook.service.SubjectService;

import java.util.List;

import static science.workbook.dto.api.ApiServerMessage.목차_리스트;
import static science.workbook.dto.api.ApiServerMessage.목차_삭제_성공;
import static science.workbook.dto.api.ApiServerMessage.목차_생성_성공;

@Slf4j
@RestController("/gradle")
@RequiredArgsConstructor
public class GradleController {
    private final GradleService gradleService;
    private final SubjectService subjectService;

    @PostMapping("/user")
    public Api<GradleDto> getGradle(@Validated @RequestBody FindGradleDto dto) {
        Subject subject = subjectService.findSubject(dto.getSubjectName());
        List<Gradle> allGradle = gradleService.findAllGradle(subject);
        return new Api<>(new GradleDto(allGradle), 목차_리스트);
    }

    @PostMapping("/manager")
    public Api<ApiMessage> createGradle(@Validated @RequestBody CreateGradleDto dto) {
        Subject subject = subjectService.findSubject(dto.getSubjectName());
        gradleService.addGradle(dto.getGradleName(), subject);
        return new Api<>(목차_생성_성공);
    }

    @DeleteMapping("/manager")
    public Api<ApiMessage> deleteGradle(@Validated @RequestBody DeleteGradleDto dto) {
        gradleService.deleteGradle(dto.getGradleName());
        return new Api<>(목차_삭제_성공);
    }
}
