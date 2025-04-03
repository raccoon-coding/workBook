package science.workbook.dto.toController;

import science.workbook.domain.User;

public record RefreshDto(User user, String jwt) {
}
