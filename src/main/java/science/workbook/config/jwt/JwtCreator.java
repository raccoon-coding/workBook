package science.workbook.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import science.workbook.dto.response.TokenDto;
import science.workbook.dto.toService.CreateTokenDto;
import science.workbook.exception.token.NotHaveToken;
import science.workbook.exception.constant.ApiErrorMessage;

import javax.crypto.SecretKey;
import java.util.Date;

import static science.workbook.config.jwt.JwtUtil.HALF_HOUR;
import static science.workbook.config.jwt.JwtUtil.HEADER;
import static science.workbook.config.jwt.JwtUtil.ONE_HOUR;
import static science.workbook.config.jwt.JwtUtil.REFRESH_COUNT;
import static science.workbook.config.jwt.JwtUtil.SUBJECT_ACCESS;
import static science.workbook.config.jwt.JwtUtil.SUBJECT_REFRESH;
import static science.workbook.config.jwt.JwtUtil.TOKEN_PREFIX;
import static science.workbook.config.jwt.JwtUtil.USER_UUID;

public interface JwtCreator {
    SecretKey getKey();

    default TokenDto createToken(CreateTokenDto tokenDto){
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + HALF_HOUR);
        Date refreshTokenExpiresIn = new Date(now + ONE_HOUR);

        String accessToken = createAccessToken(tokenDto, accessTokenExpiresIn);
        String refreshToken = createRefreshToken(tokenDto, refreshTokenExpiresIn);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    default Jws<Claims> verify(String jwt, String type){
        return Jwts.parser()
                .verifyWith(getKey())
                .requireSubject(type)
                .build()
                .parseSignedClaims(jwt);
    }

    default String getToken(HttpServletRequest request){
        String token = request.getHeader(HEADER);

        if(!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        throw new NotHaveToken(ApiErrorMessage.토큰_요청.getMessage());
    }

    private String createAccessToken(CreateTokenDto tokenDto, Date tokenExpiresIn) {
        String token = Jwts.builder()
                .subject(SUBJECT_ACCESS)
                .claim(USER_UUID, tokenDto.userId())
                .issuedAt(new Date())
                .expiration(tokenExpiresIn)
                .signWith(getKey())
                .compact();
        return TOKEN_PREFIX + token;
    }

    private String createRefreshToken(CreateTokenDto tokenDto, Date tokenExpiresIn) {
        String token = Jwts.builder()
                .subject(SUBJECT_REFRESH)
                .claim(USER_UUID, tokenDto.userId())
                .claim(REFRESH_COUNT, tokenDto.refreshCount() + 1)
                .expiration(tokenExpiresIn)
                .signWith(getKey())
                .compact();
        return TOKEN_PREFIX + token;
    }
}
