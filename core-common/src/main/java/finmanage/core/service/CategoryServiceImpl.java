package finmanage.core.service;


import finmanage.core.dto.CategoryRequestDto;
import finmanage.core.dto.CategoryResponseDto;
import finmanage.core.entity.Category;
import finmanage.core.entity.User;
import finmanage.core.exception.BadRequestException;
import finmanage.core.exception.ResourceNotFoundException;
import finmanage.core.exception.UnauthorizedOperationException;
import finmanage.core.repository.CategoryRepository;
import finmanage.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository usuarioRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository usuarioRepository) {
        this.categoryRepository = categoryRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto requestDto, Long userId) {
        User user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (categoryRepository.findByNameAndUserIdAndType(requestDto.getName(), userId, requestDto.getType()).isPresent()) {
            throw new BadRequestException("Category with name '" + requestDto.getName() + "' and type '" + requestDto.getType() + "' already exists for this user.");
        }

        Category category = new Category();
        category.setName(requestDto.getName());
        category.setTypeCategory(requestDto.getType());
        category.setUsuario(user);

        Category savedCategory = categoryRepository.save(category);
        return mapToResponseDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategoriesByUserId(Long userId) {
        if (!usuarioRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return categoryRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(Long categoryId, Long userId) {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId + " for user " + userId));
        return mapToResponseDto(category);
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto requestDto, Long userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        if (!category.getUsuario().getId().equals(userId)) {
            throw new UnauthorizedOperationException("User not authorized to update this category.");
        }

        // Check for duplicate name/type if changed
        if ((!category.getName().equals(requestDto.getName()) || !category.getTypeCategory().equals(requestDto.getType())) &&
                categoryRepository.findByNameAndUserIdAndType(requestDto.getName(), userId, requestDto.getType()).isPresent()) {
            throw new BadRequestException("Another category with name '" + requestDto.getName() + "' and type '" + requestDto.getType() + "' already exists for this user.");
        }

        category.setName(requestDto.getName());
        category.setTypeCategory(requestDto.getType());

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponseDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        if (!category.getUsuario().getId().equals(userId)) {
            throw new UnauthorizedOperationException("User not authorized to delete this category.");
        }
        // Consider implications: what happens to transactions using this category?
        // Typically, you might prevent deletion if in use, or allow and nullify category_id in transactions (requires nullable FK).
        categoryRepository.delete(category);
    }

    private CategoryResponseDto mapToResponseDto(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setType(category.getTypeCategory());
        dto.setUserId(category.getUsuario().getId());
        return dto;
    }
}