package science.workbook.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import science.workbook.domain.EmailType;
import science.workbook.exception.service.mail.MailSendException;
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
import static science.workbook.exception.constant.ApiErrorMessage.이메일_전송_에러;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
    @InjectMocks
    private MailService mailService;

    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private SpringTemplateEngine templateEngine;
    @Mock
    private EmailTypeRepositoryValid emailRepository;

    @Test
    void 메일_보내기_확인() throws MessagingException {
        String toEmail = "test@example.com";
        String title = "Test Email";
        String content = "This is a test email.";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        mailService.sendEmailNotice(toEmail, title, content);

        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void 메일_보내기_실패(){
        String toEmail = "test@example.com";
        String title = "Test Email";
        String content = "This is a test email.";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailSendException(이메일_전송_에러)).when(javaMailSender).send(mimeMessage);

        assertThrows(MailSendException.class, () -> mailService.sendEmailNotice(toEmail, title, content));
    }

    @Test
    void EmailType_생성_확인() {
        String email = "test@example.com";
        doNothing().when(emailRepository).createEmailType(any(EmailType.class));

        EmailType result = mailService.createVerificationCode(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(emailRepository, times(1)).createEmailType(any(EmailType.class));
    }

    @Test
    void Email_코드내용_확인() {
        String name = "User";
        String code = "123456";
        String expectedHtml = "<html>Email Template</html>";
        when(templateEngine.process(eq("EmailValid"), any(Context.class))).thenReturn(expectedHtml);

        String result = mailService.setContextValidEmail(name, code);

        assertEquals(expectedHtml, result);
        verify(templateEngine, times(1)).process(eq("EmailValid"), any(Context.class));
    }

    @Test
    void Email_비밀번호_변경내용_확인() {
        String name = "User";
        String newPassword = "new password";
        String expectedHtml = "<html>Email Template</html>";
        when(templateEngine.process(eq("FindPassword"), any(Context.class))).thenReturn(expectedHtml);

        String result = mailService.setContextFindPassword(name, newPassword);

        assertEquals(expectedHtml, result);
        verify(templateEngine, times(1)).process(eq("FindPassword"), any(Context.class));
    }
}
