package science.workbook.controller.apiController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import science.workbook.dto.api.Api;
import science.workbook.dto.request.GetNewUserDto;
import science.workbook.dto.response.JoinCompleteDto;
import science.workbook.dto.toController.JoinUserInfoDtoToController;
import science.workbook.service.UserService;

import static science.workbook.dto.api.ApiServerMessage.회원가입_성공;
import static science.workbook.exception.constant.ApiErrorMessage.가입실패;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;

    @PostMapping("/join")
    public Api<JoinCompleteDto> joinUserDefault(GetNewUserDto dto) {
        if(userService.validUserEmailAndName(dto.getUserEmail(), dto.getUserName())){
            return new Api<>(가입실패);
        }
        JoinUserInfoDtoToController userInfo = userService.createNewUser(dto);
        JoinCompleteDto completeDto = new JoinCompleteDto(userInfo);

        return new Api<>(completeDto, 회원가입_성공);
    }
}
