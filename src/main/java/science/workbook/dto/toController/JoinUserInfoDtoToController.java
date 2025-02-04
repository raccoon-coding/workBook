package science.workbook.dto.toController;

import lombok.Getter;
import science.workbook.dto.toService.CreateNewUserDtoToService;

@Getter
public class JoinUserInfoDtoToController {
    private final String userEmail;
    private final String username;
    private final String userType;
    private final String ssoType;

    public JoinUserInfoDtoToController(CreateNewUserDtoToService dto) {
        this.userEmail = dto.email();
        this.username = dto.name();
        this.userType = dto.userType().toString();
        this.ssoType = dto.ssoType().toString();
    }
}
