package science.workbook.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;
import science.workbook.config.security.SecurityProperties;

import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtProvider jwtProvider;
    private final SecurityProperties securityProperties;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        if(verifySkipFilterChain(requestURI)){
            chain.doFilter(request, response);
            return;
        }

        String token = jwtProvider.getToken(httpRequest);
        validToken(token);
        chain.doFilter(request, response);
    }

    private Boolean verifySkipFilterChain(String requestUrl) {
        log.info("request URL : {}", requestUrl);
        return securityProperties.getSkipPatterns().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestUrl));
    }

    private void validToken(String token) {
        if(!verifyToken(token)){
            log.info("토큰이 유효하지 않습니다.");
            return;
        }
        Date iat = jwtProvider.getIat(token);

        Authentication authentication = jwtProvider.getAuthentication(token, iat);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("토큰 유효하다");
    }

    private Boolean verifyToken(String token) {
        return token != null && jwtProvider.validateAccessToken(token);
    }
}
