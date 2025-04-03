package science.workbook.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import science.workbook.exception.token.InvalidRefreshTokenException;

import java.math.BigInteger;
import java.util.Date;

import static science.workbook.config.jwt.JwtUtil.REFRESH_Id;
import static science.workbook.config.jwt.JwtUtil.SUBJECT_REFRESH;
import static science.workbook.config.jwt.JwtUtil.USER_UUID;
import static science.workbook.exception.constant.ApiErrorMessage.다른_토큰;

public interface RefreshToken extends JwtCreator {
    default String getRefreshUserPk(String token) {
        return verify(token, SUBJECT_REFRESH)
                .getPayload()
                .get(USER_UUID)
                .toString();
    }

    default BigInteger getRefreshId(String token) {
        return (BigInteger) verify(token, SUBJECT_REFRESH)
                .getPayload()
                .get(REFRESH_Id);
    }

    default Boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claims = verify(token, SUBJECT_REFRESH);
            return !claims.getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            throw new InvalidRefreshTokenException(다른_토큰);
        }
    }
}
