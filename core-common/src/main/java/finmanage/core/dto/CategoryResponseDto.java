package finmanage.core.dto;

import finmanage.core.enums.CategoryType;
import lombok.Data;

@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
    private CategoryType type;
    private Long userId;
}