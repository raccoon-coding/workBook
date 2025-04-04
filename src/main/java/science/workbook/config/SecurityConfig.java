package science.workbook.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import science.workbook.config.jwt.JwtAuthenticationFilter;
import science.workbook.config.jwt.JwtProvider;
import science.workbook.config.security.SecurityProperties;

import static science.workbook.domain.UserType.Academy;
import static science.workbook.domain.UserType.Student;
import static science.workbook.domain.UserType.Teacher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final SecurityProperties securityProperties;

    private final String student = Student.name();
    private final String teacher = Teacher.name();
    private final String academy = Academy.name();

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/join", "/login", "/refresh", "/validEmail", "/findUserEmail", "/findUserPassword").permitAll()
                        .requestMatchers("/user/**").hasAnyAuthority(student, teacher, academy)
                        .requestMatchers("/student/**").hasAnyAuthority(student)
                        .requestMatchers("/teacher/**").hasAnyAuthority(teacher, academy)
                        .requestMatchers("/academy/**").hasAuthority(academy)
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, securityProperties),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
