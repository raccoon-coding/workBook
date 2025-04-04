package science.workbook.controller.apiController;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import science.workbook.domain.User;
import science.workbook.dto.api.Api;
import science.workbook.dto.api.ApiMessage;
import science.workbook.dto.request.ChangePasswordDto;
import science.workbook.dto.response.UserInfoDto;
import science.workbook.dto.toService.ChangeUserPasswordDtoToService;
import science.workbook.exception.CustomException;
import science.workbook.exception.controller.FailDeleteUserException;
import science.workbook.service.FileService;
import science.workbook.service.MailService;
import science.workbook.service.RefreshService;
import science.workbook.service.UserService;

import static science.workbook.dto.api.ApiServerMessage.비밀번호_변경_성공;
import static science.workbook.dto.api.ApiServerMessage.유저정보_성공;
import static science.workbook.dto.api.ApiServerMessage.회원탈퇴_성공;
import static science.workbook.util.UserUtil.getUser;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FileService fileService;
    private final RefreshService refreshService;
    private final MailService mailService;

    @GetMapping("/info")
    public Api<UserInfoDto> getUserInfo() {
        User user = getUser();
        UserInfoDto userInfoDto = new UserInfoDto(user);

        return new Api<>(userInfoDto, 유저정보_성공);
    }

    @DeleteMapping
    public Api<ApiMessage> deleteUser() {
        User deleteUser = getUser();
        String deleteUserEmail = deleteUser.getEmail();
        String deleteUserName = deleteUser.getName();

        try {
            mailService.deleteEmailType(deleteUserEmail);
            refreshService.deleteRefresh(deleteUserEmail);
            fileService.deleteUserDirectory(deleteUserName);
            userService.deleteUser(deleteUser);
        } catch (CustomException e) {
            throw new FailDeleteUserException(e.getApiMessage());
        }

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
}
