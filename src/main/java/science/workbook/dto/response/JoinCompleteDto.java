package science.workbook.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import science.workbook.domain.User;

@Getter
@AllArgsConstructor
public class JoinCompleteDto {
    private final String userEmail;
    private final String username;
    private final String userType;

    public JoinCompleteDto(User user) {
        this.userEmail = user.getEmail();
        this.username = user.getName();
        this.userType = user.getUserType().toString();
    }
}
