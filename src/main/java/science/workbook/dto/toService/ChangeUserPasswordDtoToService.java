package science.workbook.dto.toService;

import science.workbook.domain.User;

public record ChangeUserPasswordDtoToService(User user, String oldPassword, String newPassword) {
}
