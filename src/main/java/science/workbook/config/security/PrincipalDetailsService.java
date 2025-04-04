package science.workbook.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import science.workbook.domain.User;
import science.workbook.exception.token.ExpiredToken;
import science.workbook.repository.repositoryValid.UserRepositoryValid;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static java.time.LocalDateTime.now;
import static science.workbook.exception.constant.ApiErrorMessage.토큰_재로그인;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService {
    private final UserRepositoryValid repository;

    public UserDetails loadUserByUserId(String userId, Date iat) throws UsernameNotFoundException {
        User user = repository.findByUserId(userId);
        if(iat.before(tranformdate())){
            throw new ExpiredToken(토큰_재로그인);
        }
        return new PrincipalDetails(user);
    }

    private Date tranformdate() {
        ZonedDateTime updateTime = now().atZone(ZoneId.systemDefault());
        return Date.from(updateTime.toInstant());
    }
}
