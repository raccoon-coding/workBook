package science.workbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import science.workbook.domain.User;
import science.workbook.domain.UserType;
import science.workbook.dto.request.GetNewUserDto;
import science.workbook.dto.toService.CreateNewUserDto;
import science.workbook.repository.repositoryValid.UserRepositoryValid;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryValid repository;

    @Transactional
    public void createNewUser(GetNewUserDto controllerDto) {
        UserType userType = UserType.findUserType(controllerDto.getUserType());
        CreateNewUserDto dto = CreateNewUserDto.createDefaultSsoType(controllerDto.getUserEmail(),
                controllerDto.getUserName(), controllerDto.getUserPassword(), userType);

        repository.createNewUser(dto);
    }

    public User findByUserEmail(String email) {
        return repository.findByUserEmail(email);
    }
}
