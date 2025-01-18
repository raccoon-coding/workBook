package science.workbook.dto.toService;

import science.workbook.domain.User;

public record ChangeUserPasswordDto(User user, String oldPassword, String newPassword) {
}
