package science.workbook.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import science.workbook.config.jwt.JwtProvider;
import science.workbook.domain.EmailType;
import science.workbook.domain.SsoType;
import science.workbook.domain.User;
import science.workbook.domain.UserType;
import science.workbook.dto.request.GetNewUserDto;
import science.workbook.dto.response.TokenDto;
import science.workbook.dto.toService.ChangeUserPasswordDtoToService;
import science.workbook.dto.toService.CreateNewUserDtoToService;
import science.workbook.dto.toService.JoinUserDtoToService;
import science.workbook.repository.repositoryValid.UserRepositoryValid;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepositoryValid repository;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private BCryptPasswordEncoder encoder;

    private final String userEmail = "admin@gmail.com";
    private final String userName = "admin";
    private final String password = "1234";

    @Test
    void 새로운_유저_생성() {
        GetNewUserDto getNewUserDto = 유저_데이터_생성();
        User newUser = stubbing();

        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(repository.findByUserEmail(userEmail)).thenReturn(newUser);

        userService.createNewUser(getNewUserDto, null);

        User user = userService.findByUserEmail(userEmail);
        assertThat(user.getEmail()).isEqualTo(userEmail);
    }

    @Test
    void 이메일로_유저_찾기() {
        User user = stubbing();
        when(repository.findByUserEmail(userEmail)).thenReturn(user);

        User foundUser = userService.findByUserEmail(userEmail);
        assertThat(foundUser.getEmail()).isEqualTo(userEmail);
    }

    @Test
    void 유저_삭제() {
        User user = stubbing();

        userService.deleteUser(user);
        verify(repository, times(1)).deleteUser(user);
    }

    @Test
    void 여러_이메일_삭제() {
        List<String> emails = List.of("user1@gmail.com", "user2@gmail.com");

        userService.deleteUsersByEmails(emails);
        verify(repository, times(1)).deleteEmails(emails);
    }

    @Test
    void 비밀번호_변경() {
        User user = stubbing();

        ChangeUserPasswordDtoToService dto = new ChangeUserPasswordDtoToService(user, password, "newPassword");

        when(encoder.matches(password, user.getPassword())).thenReturn(true);
        when(encoder.encode(dto.newPassword())).thenReturn("encodedNewPassword");

        userService.changeUserPassword(dto);

        assertThat(user.getPassword()).isEqualTo("encodedNewPassword");
        verify(repository, times(1)).changeUserPassword(user);
    }

    @Test
    void 비밀번호_찾기() {
        User user = stubbing();
        GetNewUserDto getNewUserDto = 유저_데이터_생성();
        EmailType emailType = new EmailType(userEmail);

        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(repository.findByUserEmail(userEmail)).thenReturn(user);
        userService.createNewUser(getNewUserDto, emailType);

        when(repository.findByUserEmail(userEmail)).thenReturn(user);
        when(encoder.encode(anyString())).thenReturn("encodedNewPassword");

        String newPassword = userService.findPasswordByUserEmail(userEmail, userName);

        assertThat(newPassword).isNotEmpty();
        verify(repository, times(1)).findByUserEmail(userEmail);
    }

    @Test
    void 로그인_성공() {
        User user = stubbing();
        JoinUserDtoToService dto = new JoinUserDtoToService(userEmail, password);

        when(repository.findByUserEmail(userEmail)).thenReturn(user);
        when(encoder.matches(password, user.getPassword())).thenReturn(true);
        when(jwtProvider.createToken(any())).thenReturn(
                TokenDto.builder()
                        .accessToken("accessToken")
                        .refreshToken("refreshToken")
                        .build());

        TokenDto token = userService.loginUser(dto);

        assertThat(token).isNotNull();
        assertThat(token.getAccessToken()).isEqualTo("accessToken");
    }

    @Test
    void 이름_비교() {
        User user = stubbing();

        boolean result = userService.isEqualUserName(user, userName);
        assertThat(result).isTrue();
    }

    @Test
    void 이메일_이름_검증() {
        User user = stubbing();
        when(repository.findByUserEmail(userEmail)).thenReturn(user);

        boolean result = userService.validUserEmailAndName(userEmail, userName);
        assertThat(result).isTrue();
    }

    GetNewUserDto 유저_데이터_생성() {
        return new GetNewUserDto(userEmail, userName, password, "Student", "Default");
    }

    User stubbing() {
        CreateNewUserDtoToService dto = new CreateNewUserDtoToService(userEmail, userName, password, UserType.Student, SsoType.Default);
        EmailType emailType = new EmailType(userEmail);
        String code = emailType.getCode();
        emailType.checkEmail(code);
        return new User(dto, emailType);
    }
}
