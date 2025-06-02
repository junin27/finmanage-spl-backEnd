package finmanage.core.service;

import finmanage.core.dto.CategoryRequestDto;
import finmanage.core.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto requestDto, Long userId);
    List<CategoryResponseDto> getCategoriesByUserId(Long userId);
    CategoryResponseDto getCategoryById(Long categoryId, Long userId);
    CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto requestDto, Long userId);
    void deleteCategory(Long categoryId, Long userId);


}
