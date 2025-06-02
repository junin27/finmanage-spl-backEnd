package finmanage.core.repository;


import finmanage.core.dto.CategoryRequestDto;
import finmanage.core.dto.CategoryResponseDto;
import finmanage.core.entity.Category;
import finmanage.core.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(Long userId);
    Optional<Category> findByIdAndUserId(Long id, Long userId);
    Optional<Category> findByNameAndUserIdAndType(String name, Long userId, CategoryType type);


}