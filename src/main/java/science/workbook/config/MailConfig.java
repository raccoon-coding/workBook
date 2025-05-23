package science.workbook.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
    private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String MAIL_SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    private static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
    private static final String MAIL_SMTP_WIRTETIMEOUT = "mail.smtp.writetimeout";

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;
    @Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
    private int connectionTimeout;
    @Value("${spring.mail.properties.mail.starttls.enable}")
    private boolean startTlsEnable;
    @Value("${spring.mail.properties.mail.starttls.required}")
    private boolean startTlsRequired;
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeout;
    @Value("${spring.mail.properties.mail.smtp.writetimeout}")
    private int writeTimeout;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);

        Properties properties = getMailProperties(javaMailSender);

        javaMailSender.setJavaMailProperties(properties);
        javaMailSender.setDefaultEncoding("UTF-8");

        return javaMailSender;
    }

    private Properties getMailProperties(JavaMailSenderImpl javaMailSender) {
        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put(MAIL_SMTP_AUTH, auth);
        properties.put(MAIL_SMTP_STARTTLS_ENABLE, startTlsEnable);
        properties.put(MAIL_SMTP_STARTTLS_REQUIRED, startTlsRequired);
        properties.put(MAIL_CONNECTION_TIMEOUT, connectionTimeout);
        properties.put(MAIL_SMTP_TIMEOUT, timeout);
        properties.put(MAIL_SMTP_WIRTETIMEOUT, writeTimeout);
        return properties;
    }
}
