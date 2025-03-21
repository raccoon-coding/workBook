package science.workbook.controller.apiController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import science.workbook.config.SecurityConfig;
import science.workbook.config.jwt.JwtProvider;
import science.workbook.config.security.SecurityProperties;
import science.workbook.domain.EmailType;
import science.workbook.domain.SsoType;
import science.workbook.domain.User;
import science.workbook.domain.UserType;
import science.workbook.dto.request.FindUserEmailDto;
import science.workbook.dto.request.FindUserPasswordDto;
import science.workbook.dto.request.LoginUserDto;
import science.workbook.dto.request.ValidEmailDto;
import science.workbook.dto.response.TokenDto;
import science.workbook.dto.toService.CreateNewUserDtoToService;
import science.workbook.service.MailService;
import science.workbook.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static science.workbook.dto.api.ApiServerMessage.로그인_성공;
import static science.workbook.dto.api.ApiServerMessage.새로운_비밀번호_생성;
import static science.workbook.dto.api.ApiServerMessage.이메일_존재_성공;
import static science.workbook.dto.api.ApiServerMessage.이메일_확인_성공;
import static science.workbook.dto.api.ApiServerMessage.회원가입_성공;

@WebMvcTest(MainController.class)
@Import(SecurityConfig.class)
@ExtendWith(MockitoExtension.class)
class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuditingHandler auditingHandler;
    @MockBean
    private UserService userService;
    @MockBean
    private MailService mailService;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private SecurityProperties securityProperties;
    @Autowired
    private MainController mainController;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void 회원가입_API_확인() throws Exception {
        EmailType emailType = new EmailType("test@gmail.com");
        CreateNewUserDtoToService newUserDto = new CreateNewUserDtoToService("test@gmail.com",
                "TestName",
                "TestPassword",
                UserType.Student,
                SsoType.Default);
        User newUser = new User(newUserDto, emailType);

        when(userService.validUserEmailAndName(any(), any())).thenReturn(false);
        when(mailService.createVerificationCode(any())).thenReturn(emailType);
        when(userService.createNewUser(any(), any())).thenReturn(newUser);
        when(userService.findByUserEmail(any())).thenReturn(newUser);
        when(userService.isEqualUserName(any(), any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userEmail\":\"test@gmail.com\", " +
                                "\"userName\":\"testUser\", " +
                                "\"userPassword\":\"password\",  " +
                                "\"userType\":\"Student\", " +
                                "\"provider\":\"Default\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(회원가입_성공.getMessage()));
    }

    @Test
    @DisplayName("스케쥴러 기능 테스트")
    void 스케쥴러_기능_정상동작_확인() {
        // given
        List<String> expiredEmails = Arrays.asList("user1@example.com", "user2@example.com");
        when(mailService.deleteEmailTypeByExpiresTimeBefore()).thenReturn(expiredEmails);

        // when
        mainController.deleteNotJoinUser();

        // then
        verify(userService, times(1)).deleteUsersByEmails(expiredEmails);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void 로그인_API_확인() throws Exception {
        LoginUserDto dto = new LoginUserDto("test@gmail.com" ,"testPassword");
        TokenDto tokenDto = TokenDto.builder()
                .accessToken("token123")
                .refreshToken("tokenRefresh").build();

        when(userService.loginUser(any())).thenReturn(tokenDto);

        mockMvc.perform(post("/login")
                        .with(csrf()) // CSRF 토큰 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(로그인_성공.getMessage()));
    }

    @Test
    @DisplayName("이메일 찾기 검증 테스트")
    void 이메일_찾기_API_확인() throws Exception {
        FindUserEmailDto dto = new FindUserEmailDto("test@example.com", "TestUser");
        EmailType emailType = new EmailType("test@example.com");
        CreateNewUserDtoToService newUserDto = new CreateNewUserDtoToService("test@example.com", "TestUser", "TestPassword", UserType.Student, SsoType.Default);
        User user = new User(newUserDto, emailType);

        when(userService.findByUserEmail(anyString())).thenReturn(user);
        when(userService.isEqualUserName(any(User.class), anyString())).thenReturn(true);

        mockMvc.perform(post("/findUserEmail")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(이메일_존재_성공.getMessage()));
    }

    @Test
    @DisplayName("비밀번호 찾기 검증 테스트")
    void 비밀번호_찾기_API_확인() throws Exception {
        FindUserPasswordDto dto = new FindUserPasswordDto("test@example.com", "TestUser");

        when(userService.findPasswordByUserEmail(anyString(), anyString())).thenReturn("newPassword123");

        mockMvc.perform(post("/findUserPassword")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(새로운_비밀번호_생성.getMessage()));
    }

    @Test
    @DisplayName("이메일 인증 코드 검증 테스트")
    void 이메일_검증_API_확인() throws Exception {
        ValidEmailDto dto = new ValidEmailDto("test@example.com", "123456");

        when(mailService.validEmailCode(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/validEmail")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(이메일_확인_성공.getMessage()));
    }
}
