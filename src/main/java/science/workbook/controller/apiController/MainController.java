package science.workbook.controller.apiController;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import science.workbook.domain.EmailType;
import science.workbook.domain.Refresh;
import science.workbook.domain.User;
import science.workbook.dto.api.Api;
import science.workbook.dto.api.ApiMessage;
import science.workbook.dto.request.FindUserEmailDto;
import science.workbook.dto.request.FindUserPasswordDto;
import science.workbook.dto.request.GetNewUserDto;
import science.workbook.dto.request.LoginUserDto;
import science.workbook.dto.request.ValidEmailDto;
import science.workbook.dto.response.JoinCompleteDto;
import science.workbook.dto.response.TokenDto;
import science.workbook.dto.toController.LoginDto;
import science.workbook.dto.toController.RefreshDto;
import science.workbook.dto.toService.JoinUserDtoToService;
import science.workbook.dto.toService.SaveRefreshTokenDtoToService;
import science.workbook.service.MailService;
import science.workbook.service.RefreshService;
import science.workbook.service.UserService;

import java.util.List;

import static science.workbook.dto.api.ApiServerMessage.로그인_성공;
import static science.workbook.dto.api.ApiServerMessage.새로운_비밀번호_생성;
import static science.workbook.dto.api.ApiServerMessage.이메일_존재_성공;
import static science.workbook.dto.api.ApiServerMessage.이메일_확인_성공;
import static science.workbook.dto.api.ApiServerMessage.토큰_재발급_성공;
import static science.workbook.dto.api.ApiServerMessage.회원가입_성공;
import static science.workbook.exception.constant.ApiErrorMessage.가입실패;
import static science.workbook.exception.constant.ApiErrorMessage.유저찾기실패;
import static science.workbook.exception.constant.ApiErrorMessage.이메일_코드_에러;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final RefreshService refreshService;
    private final MailService mailService;

    @PostMapping("/join")
    public Api<JoinCompleteDto> joinUserDefault(@Validated @RequestBody GetNewUserDto dto) {
        if(userService.validUserEmailAndName(dto.getUserEmail(), dto.getUserName())){
            return new Api<>(가입실패);
        }

        EmailType emailType = mailService.createVerificationCode(dto.getUserEmail());
        String content = mailService.setContextValidEmail(dto.getUserName(), emailType.getCode());
        mailService.sendEmailNotice(emailType.getEmail(), "회원 가입 인증", content);
        Refresh refresh = refreshService.createRefresh(dto.getUserName(), dto.getUserEmail());
        User newUser = userService.createNewUser(dto, emailType, refresh);

        JoinCompleteDto completeDto = new JoinCompleteDto(newUser);
        return new Api<>(completeDto, 회원가입_성공);
    }

    @PostMapping("/login")
    public Api<TokenDto> loginUser(@Validated @RequestBody LoginUserDto dto) {
        LoginDto loginDto = userService.loginUser(JoinUserDtoToService.of(dto));
        refreshService.mergeRefreshToken(loginDto.refreshDto());
        return new Api<>(loginDto.tokenDto(), 로그인_성공);
    }

    @PostMapping("/validEmail")
    public Api<ApiMessage> validEmail(@Validated @RequestBody ValidEmailDto dto) {
        if(mailService.validEmailCode(dto.getUserEmail(), dto.getCode())) {
            return new Api<>(이메일_확인_성공);
        }
        return new Api<>(이메일_코드_에러);
    }

    @PostMapping("/findUserEmail")
    public Api<ApiMessage> findUserEmail(@Validated @RequestBody FindUserEmailDto dto) {
        User user = userService.findByUserEmail(dto.getUserEmail());
        if(userService.isEqualUserName(user, dto.getUserName())) {
            return new Api<>(이메일_존재_성공);
        }

        return new Api<>(유저찾기실패);
    }

    @PostMapping("/findUserPassword")
    public Api<ApiMessage> findUserPassword(@Validated @RequestBody FindUserPasswordDto dto) {
        String newPassword = userService.findPasswordByUserEmail(dto.getUserEmail(), dto.getUserName());

        String content = mailService.setContextFindPassword(dto.getUserName(), newPassword);
        mailService.sendEmailNotice(dto.getUserEmail(), "임시 비밀번호 발급", content);
        return new Api<>(새로운_비밀번호_생성);
    }

    @PatchMapping("/refresh")
    public Api<TokenDto> refreshToken(ServletRequest request) {
        RefreshDto dto = userService.findUserByToken((HttpServletRequest) request);

        TokenDto tokenDto = refreshService.reLogin(dto);
        SaveRefreshTokenDtoToService saveDto = new SaveRefreshTokenDtoToService(tokenDto.getRefreshToken(), dto.jwt());
        refreshService.mergeRefreshToken(saveDto);

        return new Api<>(tokenDto, 토큰_재발급_성공);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteNotJoinUser() {
        List<String> deleteEmails = mailService.deleteEmailTypeByExpiresTimeBefore();
        userService.deleteUsersByEmails(deleteEmails);
    }
}
