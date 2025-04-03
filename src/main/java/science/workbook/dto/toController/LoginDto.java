package science.workbook.dto.toController;

import science.workbook.dto.response.TokenDto;
import science.workbook.dto.toService.SaveRefreshTokenDtoToService;

public record LoginDto(TokenDto tokenDto, SaveRefreshTokenDtoToService refreshDto) {
}
