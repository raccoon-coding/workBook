package science.workbook.dto.response;

import lombok.Getter;
import science.workbook.domain.User;

@Getter
public class UserInfoDto {
    private final String userName;
    private final String userEmail;

    public UserInfoDto(User user) {
        this.userName = user.getName();
        this.userEmail = user.getEmail();
    }
}
