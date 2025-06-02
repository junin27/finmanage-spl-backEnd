package finmanage.core.repository;


import finmanage.core.dto.FinancialAccountRequestDto;
import finmanage.core.dto.FinancialAccountResponseDto;
import finmanage.core.entity.FinancialAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialAccountRepository extends JpaRepository<FinancialAccount, Long> {
    List<FinancialAccount> findByUserId(Long userId);
    Optional<FinancialAccount> findByIdAndUserId(Long id, Long userId);



}