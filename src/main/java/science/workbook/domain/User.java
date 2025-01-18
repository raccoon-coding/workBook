package science.workbook.domain;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import science.workbook.dto.toService.CreateNewUserDto;

import java.util.List;

@Getter
@Document(collection = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends UserDataTime {
    @Id
    private String id;

    @Email
    private String email;
    private String name;
    private String password;
    private UserType userType;
    private SsoType ssoType;

    private List<WorkBook> workBooks;

    public User(CreateNewUserDto dto) {
        this.email = dto.email();
        this.name = dto.name();
        this.password = dto.password();
        this.userType = dto.userType();
        this.ssoType = dto.ssoType();
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
