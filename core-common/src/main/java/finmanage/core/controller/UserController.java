package finmanage.core.controller;


import finmanage.core.dto.ChangePasswordRequestDto;
import finmanage.core.dto.UserResponseDto;
import finmanage.core.entity.User;
import finmanage.core.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") // Changed from "usuarios" to "users" for consistency
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getUserProfile(@AuthenticationPrincipal User currentUser) {
        // currentUser is instance of your Usuario entity which implements UserDetails
        UserResponseDto userProfile = userService.getUserProfile(currentUser.getId());
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal User currentUser,
                                                 @Valid @RequestBody ChangePasswordRequestDto alterarSenhaRequestDto) {
        userService.changePassword(currentUser.getId(), alterarSenhaRequestDto);
        return ResponseEntity.ok("Password changed successfully.");
    }
}