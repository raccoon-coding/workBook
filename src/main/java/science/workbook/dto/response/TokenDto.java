package science.workbook.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {
    public final String accessToken;
    public final String refreshToken;
}
