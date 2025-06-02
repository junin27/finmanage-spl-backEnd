package finmanage.core.controller;


import finmanage.core.dto.LoginRequestDto;
import finmanage.core.dto.TokenResponseDto;
import finmanage.core.dto.UserRegistrationRequestDto;
import finmanage.core.dto.UserResponseDto;
import finmanage.core.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRegistrationRequestDto cadastroRequestDto) {
        UserResponseDto registeredUser = userService.registerUser(cadastroRequestDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        TokenResponseDto tokenResponse = userService.loginUser(loginRequestDto);
        return ResponseEntity.ok(tokenResponse);
    }
}