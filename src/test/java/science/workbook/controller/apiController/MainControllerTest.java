package science.workbook.controller.apiController;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
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
import science.workbook.config.AsyncConfig;
import science.workbook.config.SecurityConfig;
import science.workbook.config.jwt.JwtProvider;
import science.workbook.config.security.SecurityProperties;
import science.workbook.domain.EmailType;
import science.workbook.domain.Refresh;
import science.workbook.domain.SsoType;
import science.workbook.domain.User;
import science.workbook.domain.UserType;
import science.workbook.dto.request.FindUserEmailDto;
import science.workbook.dto.request.FindUserPasswordDto;
import science.workbook.dto.request.ValidEmailDto;
import science.workbook.dto.response.TokenDto;
import science.workbook.dto.toController.LoginDto;
import science.workbook.dto.toController.RefreshDto;
import science.workbook.dto.toService.CreateNewUserDtoToService;
import science.workbook.dto.toService.JoinUserDtoToService;
import science.workbook.dto.toService.SaveRefreshTokenDtoToService;
import science.workbook.service.MailService;
import science.workbook.service.RefreshService;
import science.workbook.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static science.workbook.dto.api.ApiServerMessage.로그인_성공;
import static science.workbook.dto.api.ApiServerMessage.새로운_비밀번호_생성;
import static science.workbook.dto.api.ApiServerMessage.이메일_존재_성공;
import static science.workbook.dto.api.ApiServerMessage.이메일_확인_성공;
import static science.workbook.dto.api.ApiServerMessage.토큰_재발급_성공;
import static science.workbook.dto.api.ApiServerMessage.회원가입_성공;

@WebMvcTest(MainController.class)
@Import({SecurityConfig.class, AsyncConfig.class})
@ExtendWith(MockitoExtension.class)
class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainController mainController;

    @MockBean
    private AuditingHandler auditingHandler;
    @MockBean
    private UserService userService;
    @MockBean
    private RefreshService refreshService;
    @MockBean
    private MailService mailService;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private SecurityProperties securityProperties;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // 10개의 스레드 생성


    private String email = "test@gmail.com";
    private String name = "TestName";
    private String password = "TestPassword";

    @Test
    @DisplayName("회원가입 성공 테스트")
    void 회원가입_API_확인() throws Exception {
        EmailType emailType = new EmailType(email);
        Refresh refresh = new Refresh(name, email);
        CreateNewUserDtoToService newUserDto = new CreateNewUserDtoToService(email,
                name,
                password,
                UserType.Student,
                SsoType.Default);
        User newUser = new User(newUserDto, emailType, refresh);

        when(userService.validUserEmailAndName(any(), any())).thenReturn(false);
        when(mailService.createVerificationCode(any())).thenReturn(emailType);
        when(userService.createNewUser(any(), any(), any())).thenReturn(newUser);
        when(userService.findByUserEmail(any())).thenReturn(newUser);
        when(userService.isEqualUserName(any(), any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(회원가입_성공.getMessage()));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void 로그인_API_확인() throws Exception {
        TokenDto tokenDto = TokenDto.builder()
                .accessToken("token123")
                .refreshToken("tokenRefresh").build();
        LoginDto loginDto = new LoginDto(tokenDto, any());
        JoinUserDtoToService dto = new JoinUserDtoToService(email, password);

        when(userService.loginUser(dto)).thenReturn(loginDto);

        mockMvc.perform(post("/login")
                        .with(csrf()) // CSRF 토큰 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(로그인_성공.getMessage()));
    }

    @Test
    @DisplayName("이메일 인증 코드 검증 테스트")
    void 이메일_검증_API_확인() throws Exception {
        ValidEmailDto dto = new ValidEmailDto(email, "123456");

        when(mailService.validEmailCode(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/validEmail")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(이메일_확인_성공.getMessage()));
    }

    @Test
    @DisplayName("이메일 찾기 검증 테스트")
    void 이메일_찾기_API_확인() throws Exception {
        FindUserEmailDto dto = new FindUserEmailDto(email, name);
        EmailType emailType = new EmailType(email);
        Refresh refresh = new Refresh(name, email);
        CreateNewUserDtoToService newUserDto = new CreateNewUserDtoToService(email, name, password, UserType.Student, SsoType.Default);
        User user = new User(newUserDto, emailType, refresh);

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
        FindUserPasswordDto dto = new FindUserPasswordDto(email, name);

        when(userService.findPasswordByUserEmail(anyString(), anyString())).thenReturn("newPassword123");

        mockMvc.perform(post("/findUserPassword")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(새로운_비밀번호_생성.getMessage()));
    }

    @Test
    @DisplayName("Refresh Token으로 재 로그인 테스트")
    void Refresh토큰_재_로그인_확인() throws Exception {
        String refreshToken = "refreshToken";
        CreateNewUserDtoToService userDto = new CreateNewUserDtoToService(email, name, password, UserType.Student, SsoType.Default);
        EmailType emailType = new EmailType(email);
        Refresh refresh = new Refresh(name, email);
        User user = new User(userDto, emailType, refresh);
        RefreshDto refreshDto = new RefreshDto(user, refreshToken);
        TokenDto tokenDto = TokenDto.builder()
                .accessToken("token123")
                .refreshToken(refreshToken).build();

        when(userService.findUserByToken(any(HttpServletRequest.class))).thenReturn(refreshDto);
        when(refreshService.reLogin(refreshDto)).thenReturn(tokenDto);

        mockMvc.perform(patch("/refresh")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(토큰_재발급_성공.getMessage()));
    }

    @Test
    @DisplayName("멀티 쓰레드로 mergeRefreshToken이 실행되는지 테스트")
    void refresh토큰_동시성_확인() throws Exception {
        String refreshToken = "valid-refresh-token";
        TokenDto tokenDto = TokenDto.builder()
                .accessToken("token123")
                .refreshToken(refreshToken).build();
        CreateNewUserDtoToService userDto = new CreateNewUserDtoToService(email, name, password, UserType.Student, SsoType.Default);
        EmailType emailType = new EmailType(email);
        Refresh refresh = new Refresh(name, email);
        User user = new User(userDto, emailType, refresh);
        RefreshDto refreshDto = new RefreshDto(user, refreshToken);

        when(userService.findUserByToken(any(HttpServletRequest.class))).thenReturn(refreshDto);
        when(refreshService.reLogin(refreshDto)).thenReturn(tokenDto);

        doAnswer(invocation -> {
            System.out.println(Thread.currentThread().getName() + " - mergeRefreshToken 실행");
            Thread.sleep(500);
            return null;
        }).when(refreshService).mergeRefreshToken(any(SaveRefreshTokenDtoToService.class));

        int numberOfRequests = 10;
        CountDownLatch latch = new CountDownLatch(numberOfRequests);

        for (int i = 0; i < numberOfRequests; i++) {
            executorService.submit(() -> {
                try {
                    mockMvc.perform(patch("/refresh")
                                    .header("Authorization", "Bearer " + refreshToken)
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        verify(refreshService, times(numberOfRequests)).mergeRefreshToken(any(SaveRefreshTokenDtoToService.class));
    }

    @Test
    @DisplayName("스케쥴러 기능 테스트")
    void 스케쥴러_기능_정상동작_확인() {
        List<String> expiredEmails = Arrays.asList("user1@example.com", "user2@example.com");
        when(mailService.deleteEmailTypeByExpiresTimeBefore()).thenReturn(expiredEmails);

        mainController.deleteNotJoinUser();

        verify(userService, times(1)).deleteUsersByEmails(expiredEmails);
    }
}
