package science.workbook.util;

import org.springframework.security.core.context.SecurityContextHolder;
import science.workbook.config.security.PrincipalDetails;
import science.workbook.domain.User;

import java.util.concurrent.ThreadLocalRandom;

public class UserUtil {
    public static User getUser() {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return principalDetails.getUserData();
    }

    public static String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for(int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
}
