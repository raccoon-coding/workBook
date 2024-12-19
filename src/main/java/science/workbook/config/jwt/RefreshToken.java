package science.workbook.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import science.workbook.exception.token.InvalidRefreshTokenException;
import science.workbook.exception.token.OverRefreshCountException;
import science.workbook.exception.constant.ApiErrorMessage;

import java.util.Date;

import static science.workbook.config.jwt.JwtUtil.MAX_REFRESH;
import static science.workbook.config.jwt.JwtUtil.REFRESH_COUNT;
import static science.workbook.config.jwt.JwtUtil.SUBJECT_REFRESH;
import static science.workbook.config.jwt.JwtUtil.USER_UUID;

public interface RefreshToken extends JwtCreator {
    default String getRefreshUserPk(String token) {
        return verify(token, SUBJECT_REFRESH)
                .getPayload()
                .get(USER_UUID)
                .toString();
    }

    default Integer getRefreshCount(String token) {
        Integer count = (Integer) verify(token, SUBJECT_REFRESH)
                .getPayload()
                .get(REFRESH_COUNT);
        if(count > MAX_REFRESH) {
            throw new OverRefreshCountException(ApiErrorMessage.토큰_만료.getMessage());
        }
        return count;
    }

    default Boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claims = verify(token, SUBJECT_REFRESH);
            return !claims.getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            throw new InvalidRefreshTokenException(ApiErrorMessage.다른_토큰.getMessage());
        }
    }
}
