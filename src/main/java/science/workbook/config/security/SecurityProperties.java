package science.workbook.config.security;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityProperties {
    private List<String> skipPatterns;

    public SecurityProperties(List<String> skipPatterns) {
        this.skipPatterns = List.of("/join", "/login", "/refresh", "/validEmail", "/findUserEmail", "/findUserPassword",
                "/v3/api-docs/**", "/swagger-ui/**");
    }

    public List<String> getSkipPatterns() {
        return skipPatterns.stream().toList();
    }
}
