package science.workbook.controller.apiController;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import science.workbook.domain.Gradle;
import science.workbook.domain.Subject;
import science.workbook.domain.User;
import science.workbook.dto.api.Api;
import science.workbook.dto.api.ApiMessage;
import science.workbook.dto.request.ChangePasswordDto;
import science.workbook.dto.request.FindGradleDto;
import science.workbook.dto.response.GradleDto;
import science.workbook.dto.response.SubjectsDto;
import science.workbook.dto.response.UserInfoDto;
import science.workbook.dto.toService.ChangeUserPasswordDtoToService;
import science.workbook.service.GradleService;
import science.workbook.service.SubjectService;
import science.workbook.service.UserService;
import science.workbook.service.facade.AuthFacadeService;

import java.util.List;

import static science.workbook.dto.api.ApiServerMessage.과목_리스트;
import static science.workbook.dto.api.ApiServerMessage.목차_리스트;
import static science.workbook.dto.api.ApiServerMessage.비밀번호_변경_성공;
import static science.workbook.dto.api.ApiServerMessage.유저정보_성공;
import static science.workbook.dto.api.ApiServerMessage.회원탈퇴_성공;
import static science.workbook.util.UserUtil.getUser;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthFacadeService authFacadeService;
    private final SubjectService subjectService;
    private final GradleService gradleService;

    @GetMapping("/info")
    public Api<UserInfoDto> getUserInfo() {
        User user = getUser();
        UserInfoDto userInfoDto = new UserInfoDto(user);

        return new Api<>(userInfoDto, 유저정보_성공);
    }

    @DeleteMapping
    public Api<ApiMessage> deleteUser() {
        User deleteUser = getUser();

        authFacadeService.deleteUser(deleteUser);

        return new Api<>(회원탈퇴_성공);
    }

    @PatchMapping("/password")
    public Api<ApiMessage> patchPassword(@Validated @RequestBody ChangePasswordDto dto) {
        User user = getUser();
        ChangeUserPasswordDtoToService serviceDto =
                new ChangeUserPasswordDtoToService(user, dto.getOldPassword(), dto.getOldPassword());

        userService.changeUserPassword(serviceDto);
        return new Api<>(비밀번호_변경_성공);
    }

    @GetMapping("/subject")
    public Api<SubjectsDto> findAllSubject() {
        List<Subject> allSubjects = subjectService.findAllSubjects();
        SubjectsDto dto = new SubjectsDto(allSubjects);
        return new Api<> (dto, 과목_리스트);
    }

    @PostMapping("/gradle")
    public Api<GradleDto> getGradle(@Validated @RequestBody FindGradleDto dto) {
        Subject subject = subjectService.findSubject(dto.getSubjectName());
        List<Gradle> allGradle = gradleService.findAllGradle(subject);
        return new Api<>(new GradleDto(allGradle), 목차_리스트);
    }
}
