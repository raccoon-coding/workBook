package science.workbook.domain;

import science.workbook.exception.repository.NotFoundUserType;

import java.util.Arrays;

import static science.workbook.exception.constant.ApiErrorMessage.유저타입에러;

public enum UserType {
    Student, Teacher, Academy, Manager;

    public static UserType findUserType(String userType) {
        return Arrays.stream(UserType.values())
                .filter(one -> one.name().equals(userType))
                .findFirst()
                .orElseThrow(() -> new NotFoundUserType(유저타입에러));
    }
}
