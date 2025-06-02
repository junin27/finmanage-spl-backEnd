package finmanage.core.service; // Pacote em inglês


import finmanage.core.dto.*;

public interface UserService {

    UserResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto);
    TokenResponseDto loginUser(LoginRequestDto loginRequestDto);
    UserResponseDto getUserProfile(Long userId);
    void changePassword(Long userId, ChangePasswordRequestDto changePasswordRequestDto);

}