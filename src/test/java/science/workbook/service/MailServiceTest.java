package science.workbook.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import science.workbook.domain.EmailType;
import science.workbook.repository.repositoryValid.EmailTypeRepositoryValid;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private SpringTemplateEngine templateEngine;
    @Mock
    private EmailTypeRepositoryValid emailRepository;
    @InjectMocks
    private MailService mailService;

    @Test
    void sendEmailNotice_Success() throws MessagingException {
        // given
        String toEmail = "test@example.com";
        String title = "Test Email";
        String content = "This is a test email.";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // when
        mailService.sendEmailNotice(toEmail, title, content);

        // then
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendEmailNotice_Failure() throws MessagingException {
        // given
        String toEmail = "test@example.com";
        String title = "Test Email";
        String content = "This is a test email.";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Email sending failed")).when(javaMailSender).send(mimeMessage);

        // when & then
        assertThrows(RuntimeException.class, () -> mailService.sendEmailNotice(toEmail, title, content));
    }

    @Test
    void createVerificationCode_Success() {
        // given
        String email = "test@example.com";
        EmailType emailType = new EmailType(email);
        doNothing().when(emailRepository).createEmailType(any(EmailType.class));

        // when
        EmailType result = mailService.createVerificationCode(email);

        // then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(emailRepository, times(1)).createEmailType(any(EmailType.class));
    }

    @Test
    void setContextValidEmail_Success() {
        // given
        String name = "User";
        String code = "123456";
        String expectedHtml = "<html>Email Template</html>";
        when(templateEngine.process(eq("EmailValid"), any(Context.class))).thenReturn(expectedHtml);

        // when
        String result = mailService.setContextValidEmail(name, code);

        // then
        assertEquals(expectedHtml, result);
        verify(templateEngine, times(1)).process(eq("EmailValid"), any(Context.class));
    }
}
