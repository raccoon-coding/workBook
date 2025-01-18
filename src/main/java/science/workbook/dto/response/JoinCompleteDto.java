package science.workbook.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import science.workbook.dto.toController.JoinUserInfo;

@Getter
@AllArgsConstructor
public class JoinCompleteDto {
    private final String userEmail;
    private final String username;
    private final String userType;
    private final String ssoType;

    public JoinCompleteDto(JoinUserInfo dto) {
        this.userEmail = dto.getUserEmail();
        this.username = dto.getUsername();
        this.userType = dto.getUserType();
        this.ssoType = dto.getSsoType();
    }
}
