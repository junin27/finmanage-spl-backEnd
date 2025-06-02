package finmanage.core.service;


import finmanage.core.dto.*;
import finmanage.core.entity.User;
import finmanage.core.exception.BadRequestException;
import finmanage.core.exception.ResourceNotFoundException;
import finmanage.core.exception.UserAlreadyExistsException;
import finmanage.core.repository.UserRepository;
import finmanage.core.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserServiceImpl(UserRepository usuarioRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional
    public UserResponseDto registerUser(UserRegistrationRequestDto dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException("Username " + dto.getUsername() + " is already taken.");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Email " + dto.getEmail() + " is already registered.");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setRole("ROLE_USER"); // Default role

        User savedUser = usuarioRepository.save(user);
        return mapToUsuarioResponseDto(savedUser);
    }

    @Override
    public TokenResponseDto loginUser(LoginRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        User user = (User) authentication.getPrincipal();
        return new TokenResponseDto(token, user.getId(), user.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserProfile(Long userId) {
        User user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return mapToUsuarioResponseDto(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequestDto dto) {
        User user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Incorrect current password.");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        usuarioRepository.save(user);
    }

    private UserResponseDto mapToUsuarioResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}