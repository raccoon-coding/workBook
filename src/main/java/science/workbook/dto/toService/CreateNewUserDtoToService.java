package science.workbook.dto.toService;

import science.workbook.domain.SsoType;
import science.workbook.domain.UserType;

public record CreateNewUserDtoToService(String email, String name, String password, UserType userType, SsoType ssoType) {
    public static CreateNewUserDtoToService createDefaultSsoType(String email, String name, String password, UserType userType) {
        return new CreateNewUserDtoToService(email, name, password, userType, SsoType.Default);
    }
}
