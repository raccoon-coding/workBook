package science.workbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import science.workbook.config.jwt.JwtProvider;
import science.workbook.domain.Refresh;
import science.workbook.domain.User;
import science.workbook.dto.response.TokenDto;
import science.workbook.dto.toController.RefreshDto;
import science.workbook.dto.toService.CreateTokenDtoToService;
import science.workbook.dto.toService.SaveRefreshTokenDtoToService;
import science.workbook.exception.token.InvalidRefreshTokenException;
import science.workbook.repository.repositoryValid.RefreshRepositoryValid;

import java.lang.annotation.Target;
import java.math.BigInteger;

import static science.workbook.exception.constant.ApiErrorMessage.재발급_토큰_에러;

@Slf4j @Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshService {
    private final RefreshRepositoryValid repository;
    private final JwtProvider jwtProvider;

    @Transactional
    public Refresh createRefresh(String userName, String email) {
        Refresh refresh = new Refresh(userName, email);
        repository.createToken(refresh);
        return refresh;
    }

    public TokenDto reLogin(RefreshDto dto) {
        String jwt = dto.jwt();
        User user = dto.user();
        BigInteger refreshId = jwtProvider.getRefreshId(jwt);

        Refresh refresh = repository.findById(refreshId);
        if(refresh.getToken().equals(jwt) && refresh.getUserName().equals(user.getName())) {
            return jwtProvider.createToken(new CreateTokenDtoToService(user.getId(), refreshId));
        }
        throw new InvalidRefreshTokenException(재발급_토큰_에러);
    }

    @Transactional
    public void deleteRefresh(String deleteEmail) {
        Refresh refresh = repository.findByEmail(deleteEmail);
        repository.deleteToken(refresh);
    }

    @Async("refresh token save Async")
    @Transactional
    public void mergeRefreshToken(SaveRefreshTokenDtoToService dto) {
        BigInteger refreshId = jwtProvider.getRefreshId(dto.jwt());
        Refresh refresh = repository.findById(refreshId);

        // 시간이 하루 이상 지났으면 에러 발생시켜서 재 로그인 시키자.

        refresh.rotate(dto.refreshToken());
        repository.mergeToken(refresh);
    }
}
