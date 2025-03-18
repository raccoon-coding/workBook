package science.workbook.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import science.workbook.domain.EmailType;
import science.workbook.repository.repositoryValid.EmailTypeRepositoryValid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailTypeRepositoryValid emailRepository;

    @Async
    public void sendEmailNotice(String toEmail, String title, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setText(content, true);
        try {

            javaMailSender.send(mimeMessage);
            log.info("Succeeded to send Email");
        } catch (Exception e) {
            log.info("Failed to send Email");
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public EmailType createVerificationCode(String email) {
        EmailType emailType = new EmailType(email);
        emailRepository.createEmailType(emailType);
        return emailType;
    }

    @Transactional
    public Boolean validEmailCode(String email, String code) {
        EmailType type = emailRepository.findEmailTypeByEmail(email);
        Boolean result = type.checkEmail(code);

        emailRepository.merge(type);
        return result;
    }

    @Transactional
    public List<String> deleteEmailTypeByExpiresTimeBefore() {
        return emailRepository.deleteEmailTypeByExpiresTimeBefore(LocalDateTime.now());
    }

    public String setContextValidEmail(String name, String code) {
        Context context = new Context();
        Map<String, Object> variables = Map.of(
                "name", name,
                "code", code);
        context.setVariables(variables);
        return templateEngine.process("EmailValid", context);
    }

    public String setContextFindPassword(String name, String password) {
        Context context = new Context();
        Map<String, Object> variables = Map.of(
                "name", name,
                "password", password);
        context.setVariables(variables);
        return templateEngine.process("FindPassword", context);
    }
}
