package science.workbook.dto.toService;

import science.workbook.dto.request.LoginUserDto;

public record JoinUserDtoToService(String userEmail, String userPassword) {
    public static JoinUserDtoToService of(LoginUserDto dto) {
        return new JoinUserDtoToService(dto.getUserEmail(), dto.getUserPassword());
    }
}
