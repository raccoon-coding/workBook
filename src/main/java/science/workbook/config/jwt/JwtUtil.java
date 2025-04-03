package science.workbook.config.jwt;

public interface JwtUtil {
    String TOKEN_PREFIX = "Bearer "; // 스페이스 필요함
    String HEADER = "Authorization";
    String SUBJECT_ACCESS = "WorkBook_Access_Token";
    String SUBJECT_REFRESH = "WorkBook_Refresh_Token";
    String USER_UUID = "id";
    String REFRESH_Id = "refreshId";
    Integer MAX_REFRESH = 10;
    int ONE_HOUR = 3_600_000;
    int HALF_HOUR = 1_800_000;
}
