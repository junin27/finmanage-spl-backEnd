package finmanage.core.controller;


import finmanage.core.dto.CategoryRequestDto;
import finmanage.core.dto.CategoryResponseDto;
import finmanage.core.entity.User;
import finmanage.core.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
            @Valid @RequestBody CategoryRequestDto requestDto,
            @AuthenticationPrincipal User currentUser) {
        CategoryResponseDto createdCategory = categoryService.createCategory(requestDto, currentUser.getId());
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategories(@AuthenticationPrincipal User currentUser) {
        List<CategoryResponseDto> categories = categoryService.getCategoriesByUserId(currentUser.getId());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal User currentUser) {
        CategoryResponseDto category = categoryService.getCategoryById(categoryId, currentUser.getId());
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequestDto requestDto,
            @AuthenticationPrincipal User currentUser) {
        CategoryResponseDto updatedCategory = categoryService.updateCategory(categoryId, requestDto, currentUser.getId());
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal User currentUser) {
        categoryService.deleteCategory(categoryId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}