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
import science.workbook.service.UserService;

import static science.workbook.dto.api.ApiServerMessage.비밀번호_변경_성공;
import static science.workbook.dto.api.ApiServerMessage.유저정보_성공;
import static science.workbook.dto.api.ApiServerMessage.회원탈퇴_성공;
import static science.workbook.util.UserUtil.getUser;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    public Api<UserInfoDto> getUserInfo() {
        User user = getUser();
        UserInfoDto userInfoDto = new UserInfoDto(user);

        return new Api<>(userInfoDto, 유저정보_성공);
    }

    @DeleteMapping
    public Api<ApiMessage> deleteUser() {
        User deleteUser = getUser();
        userService.deleteUser(deleteUser);

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
