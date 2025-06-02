package finmanage.core.repository;


import finmanage.core.dto.TransactionRequestDto;
import finmanage.core.dto.TransactionResponseDto;
import finmanage.core.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdOrderByDateDesc(Long userId);
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);
    List<Transaction> findByFinancialAccountIdAndUserIdOrderByDateDesc(Long financialAccountId, Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.date >= :startDate AND t.date <= :endDate ORDER BY t.date DESC")
    List<Transaction> findByUserIdAndPeriod(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.category.id = :categoryId AND t.date >= :startDate AND t.date <= :endDate ORDER BY t.date DESC")
    List<Transaction> findByUserIdAndCategoryIdAndPeriod(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


}