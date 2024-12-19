package science.workbook.domain;

import science.workbook.exception.repository.NotFoundUserType;

import java.util.Arrays;

public enum UserType {
    Student, Teacher, Academy;

    public static UserType findUserType(String userType) {
        return Arrays.stream(UserType.values())
                .filter(one -> one.name().equals(userType))
                .findFirst()
                .orElseThrow(() -> new NotFoundUserType("Invalid UserType: " + userType));
    }
}
