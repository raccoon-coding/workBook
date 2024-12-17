package science.workbook.dto.toService;

import science.workbook.domain.SsoType;
import science.workbook.domain.UserType;

public record CreateNewUserDto(String email, String name, String password, UserType userType, SsoType ssoType) {
    public static CreateNewUserDto createDefaultSsoType(String email, String name, String password, UserType userType) {
        return new CreateNewUserDto(email, name, password, userType, SsoType.Default);
    }
}
