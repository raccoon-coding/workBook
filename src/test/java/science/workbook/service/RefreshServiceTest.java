package science.workbook.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import science.workbook.config.jwt.JwtProvider;
import science.workbook.domain.EmailType;
import science.workbook.domain.Refresh;
import science.workbook.domain.SsoType;
import science.workbook.domain.User;
import science.workbook.domain.UserType;
import science.workbook.dto.response.TokenDto;
import science.workbook.dto.toController.RefreshDto;
import science.workbook.dto.toService.CreateNewUserDtoToService;
import science.workbook.dto.toService.SaveRefreshTokenDtoToService;
import science.workbook.repository.repositoryValid.RefreshRepositoryValid;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshServiceTest {
    @InjectMocks
    private RefreshService refreshService;

    @Mock
    private RefreshRepositoryValid repository;
    @Mock
    private JwtProvider jwtProvider;

    private String userName = "TestName";
    private String userEmail = "test@gamil.com";
    private String userPassword = "TestPassword";

    @Test
    @DisplayName("Refresh Token 생성 로직 확인")
    void Refresh토큰_생성() {
        Refresh refresh = refreshService.createRefresh(userName, userEmail);
        verify(repository, times(1)).createToken(refresh);
    }

    @Test
    @DisplayName("재 로그인 로직 확인")
    void 재_로그인_확인() {
        CreateNewUserDtoToService userDto = new CreateNewUserDtoToService(userEmail, userName, userPassword, UserType.Student, SsoType.Default);
        EmailType emailType = new EmailType(userEmail);
        Refresh refresh = new Refresh(userName, userEmail);
        User user = new User(userDto, emailType, refresh);
        String jwt = "refreshToken";
        refresh.rotate(jwt);
        TokenDto tokenDto = TokenDto.builder().accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        RefreshDto dto = new RefreshDto(user, jwt);

        when(jwtProvider.getRefreshId(any())).thenReturn(BigInteger.ONE);
        when(repository.findById(any())).thenReturn(refresh);
        when(jwtProvider.createToken(any())).thenReturn(tokenDto);

        TokenDto token = refreshService.reLogin(dto);
        assertThat(token).isNotNull();
        assertThat(token.getAccessToken()).isEqualTo("accessToken");
        assertThat(token.getRefreshToken()).isEqualTo("refreshToken");
    }

    @Test
    @DisplayName("Refresh 토큰 저장 확인")
    void Refresh토큰_저장_확인() {
        Refresh refresh = new Refresh(userName, userEmail);
        String jwt = "refreshToken";
        SaveRefreshTokenDtoToService dto = new SaveRefreshTokenDtoToService(jwt, jwt);

        when(jwtProvider.getRefreshId(any())).thenReturn(BigInteger.ONE);
        when(repository.findById(BigInteger.ONE)).thenReturn(refresh);

        refreshService.mergeRefreshToken(dto);

        verify(repository, times(1)).mergeToken(refresh);
    }
}
