package science.workbook.controller.apiController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import science.workbook.domain.User;
import science.workbook.dto.api.Api;
import science.workbook.dto.api.ApiMessage;
import science.workbook.dto.response.UserInfoDto;
import science.workbook.dto.toService.ChangeUserPasswordDto;
import science.workbook.service.UserService;
import science.workbook.util.UserUtil;

import static science.workbook.dto.api.ApiServerMessage.비밀번호_변경_성공;
import static science.workbook.dto.api.ApiServerMessage.유저정보_성공;
import static science.workbook.dto.api.ApiServerMessage.회원탈퇴_성공;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    public Api<UserInfoDto> getUserInfo(String userEmail) {
        User user = userService.findByUserEmail(userEmail);
        UserInfoDto userInfoDto = new UserInfoDto(user);

        return new Api<>(userInfoDto, 유저정보_성공);
    }

    @DeleteMapping
    public Api<ApiMessage> deleteUser(String userEmail) {
        User deleteUser = userService.findByUserEmail(userEmail);
        userService.deleteUser(deleteUser);

        return new Api<>(회원탈퇴_성공);
    }

    @PatchMapping("/password")
    public Api<ApiMessage> patchPassword(String oldPassword, String newPassword) {
        User user = UserUtil.getUser();
        ChangeUserPasswordDto dto = new ChangeUserPasswordDto(user, oldPassword, newPassword);

        userService.changeUserPassword(dto);

        return new Api<>(비밀번호_변경_성공);
    }
}
