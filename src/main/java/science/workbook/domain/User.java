package science.workbook.domain;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import science.workbook.dto.toService.CreateNewUserDtoToService;

import java.util.List;
import java.util.Objects;

@Getter
@Document(collection = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends UserTime {
    @Id
    private String id;
    @Email
    private String email;
    private String name;
    private String password;
    private UserType userType;
    private SsoType ssoType;
    @DBRef
    private EmailType emailType;

    @DBRef(lazy = true)
    private List<WorkBook> workBooks;

    public User(CreateNewUserDtoToService dto, EmailType emailType) {
        this.email = dto.email();
        this.name = dto.name();
        this.password = dto.password();
        this.userType = dto.userType();
        this.ssoType = dto.ssoType();
        createCode(emailType);
    }

    private void createCode(EmailType emailType) {
        this.emailType = emailType;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public boolean validUser() {
        return this.emailType.getValid();
    }

    public boolean validUsername(String name) {
        return Objects.equals(this.name, name);
    }
}
