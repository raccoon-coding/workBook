package science.workbook.repository.repositoryValid;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import science.workbook.domain.EmailType;
import science.workbook.exception.repository.NotFoundEmailTypeByEmail;
import science.workbook.repository.repositoryMongo.EmailTypeRepository;
import science.workbook.repository.repositoryMongo.LogRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataMongoTest
class EmailTypeRepositoryValidTest {
    private EmailTypeRepositoryValid repository;

    @Autowired
    public EmailTypeRepositoryValidTest(EmailTypeRepository repository) {
        this.repository = new EmailTypeRepositoryValid(repository);
    }

    @Test
    void EmailType_정상_저장() {
        // given
        EmailType emailType = new EmailType("test@example.com");

        // when
        repository.createEmailType(emailType);
        EmailType foundEmail = repository.findEmailTypeByEmail("test@example.com");

        // then
        assertNotNull(foundEmail);
        assertEquals("test@example.com", foundEmail.getEmail());
        repository.deleteEmailType(emailType);
    }

    @Test
    void 정상_수정_확인() {
        // given
        EmailType emailType = new EmailType("test@example.com");
        repository.createEmailType(emailType);

        EmailType type = repository.findEmailTypeByEmail("test@example.com");
        String code = type.getCode();
        type.checkEmail(code);

        // when
        repository.merge(type);
        EmailType updatedType = repository.findEmailTypeByEmail("test@example.com");

        // then
        assertNotNull(updatedType);
        assertTrue(updatedType.getValid());
        repository.deleteEmailType(type);
    }

    @Test
    void 만료된_이메일들_삭제() {
        // given
        LocalDateTime now = LocalDateTime.now();
        EmailType email1 = new EmailType("test1@example.com");
        EmailType email2 = new EmailType("test2@example.com");
        EmailType email3 = new EmailType("valid@example.com");

        repository.createEmailType(email1);
        repository.createEmailType(email2);
        repository.createEmailType(email3);

        // when
        List<String> deletedEmails = repository.deleteEmailTypeByExpiresTimeBefore(now.plusDays(1));

        // then
        assertEquals(3, deletedEmails.size());
        assertTrue(deletedEmails.contains("test1@example.com"));
        assertTrue(deletedEmails.contains("test2@example.com"));
        assertTrue(deletedEmails.contains("valid@example.com"));
    }

    @Test
    void 존재하는_이메일() {
        // given
        EmailType emailType = new EmailType("test@example.com");
        repository.createEmailType(emailType);

        // when
        EmailType foundEmailType = repository.findEmailTypeByEmail("test@example.com");

        // then
        assertNotNull(foundEmailType);
        assertEquals("test@example.com", foundEmailType.getEmail());
        repository.deleteEmailType(foundEmailType);
    }

    @Test
    void 존재하지_않는_이메일_예외_발생() {
        // given
        String email = "notfound@example.com";

        // when & then
        assertThrows(NotFoundEmailTypeByEmail.class,
                () -> repository.findEmailTypeByEmail(email));
    }

    @Test
    void EmailType_삭제() {
        // given
        EmailType emailType = new EmailType("test@example.com");
        repository.createEmailType(emailType);

        // when
        repository.deleteEmailType(emailType);

        // then
        assertThrows(NotFoundEmailTypeByEmail.class,
                () -> repository.findEmailTypeByEmail("test@example.com"));
    }
}

