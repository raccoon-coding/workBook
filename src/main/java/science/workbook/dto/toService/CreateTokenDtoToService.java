package science.workbook.dto.toService;

import java.math.BigInteger;

public record CreateTokenDtoToService(String userId, BigInteger refreshId) {
}
