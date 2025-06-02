package finmanage.core.dto;

import finmanage.core.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotBlank(message = "Category name is required")
    private String name;

    @NotNull(message = "Category type is required")
    private CategoryType type; // INCOME or EXPENSE
}