package science.workbook.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.Authentication;
import science.workbook.exception.token.RefreshTokenRedirect;

import java.util.Date;

import static science.workbook.config.jwt.JwtUtil.SUBJECT_ACCESS;
import static science.workbook.config.jwt.JwtUtil.USER_UUID;
import static science.workbook.exception.constant.ApiErrorMessage.토큰_재로그인;

public interface AccessToken extends JwtCreator {
    Authentication getAuthentication(String token, Date iat);

    default String getAccessTokenUserPk(String token) {
        return verify(token, SUBJECT_ACCESS)
                .getPayload()
                .get(USER_UUID)
                .toString();
    }

    default Boolean validateAccessToken(String token) {
        try {
            Jws<Claims> claims = verify(token, SUBJECT_ACCESS);
            return !claims.getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            throw new RefreshTokenRedirect(토큰_재로그인);
        }
    }

    default Date getIat(String token) {
        Jws<Claims> claims = verify(token, SUBJECT_ACCESS);
        return claims.getPayload().getIssuedAt();
    }
}
