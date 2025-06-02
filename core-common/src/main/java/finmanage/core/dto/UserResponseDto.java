package finmanage.core.dto;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
}