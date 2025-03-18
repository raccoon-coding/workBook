package science.workbook.repository.repositoryValid;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import science.workbook.domain.EmailType;
import science.workbook.exception.repository.NotFoundEmailTypeByEmail;
import science.workbook.repository.repositoryMongo.EmailTypeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static science.workbook.exception.constant.ApiErrorMessage.이메일_찾기_에러;

@Repository
@RequiredArgsConstructor
public class EmailTypeRepositoryValid {
    private final EmailTypeRepository repository;

    public void createEmailType(EmailType emailType) {
        repository.save(emailType);
    }

    public void merge(EmailType emailType) {
        repository.save(emailType);
    }

    public List<String> deleteEmailTypeByExpiresTimeBefore(LocalDateTime time) {
        List<EmailType> recentEmails = repository.findRecentEmailTypes(
                time.minusDays(1), Sort.by(Sort.Direction.DESC, "createdAt")
        );

        List<EmailType> emailsToDelete = recentEmails.stream()
                .filter(emailType -> emailType.getCreatedAt().isBefore(time) && !emailType.getValid())
                .collect(Collectors.toList());

        if (!emailsToDelete.isEmpty()) {
            repository.deleteAll(emailsToDelete);
        }
        return emailsToDelete.stream()
                .map(EmailType::getEmail)
                .collect(Collectors.toList());
    }

    public EmailType findEmailTypeByEmail(String email) {
        Optional<EmailType> optional = repository.findByEmail(email);
        if(optional.isPresent()) {
            return optional.get();
        }
        throw new NotFoundEmailTypeByEmail(이메일_찾기_에러);
    }

    public void deleteEmailType(EmailType emailType) {
        repository.delete(emailType);
    }
}
