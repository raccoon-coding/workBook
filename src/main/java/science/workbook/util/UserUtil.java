package science.workbook.util;

import org.springframework.security.core.context.SecurityContextHolder;
import science.workbook.config.security.PrincipalDetails;
import science.workbook.domain.User;

public class UserUtil {
    public static User getUser() {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return principalDetails.getUserData();
    }
}
