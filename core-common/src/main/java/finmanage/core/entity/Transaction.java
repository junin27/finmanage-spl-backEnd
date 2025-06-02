package finmanage.core.entity;

import finmanage.core.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal value;

    @Column(nullable = false)
    private LocalDate date; // Data da transação

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type; // RECEITA ou DESPESA

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_account_id", nullable = false)
    private FinancialAccount financialAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(updatable = false)
    private LocalDateTime dateCreate;
    private LocalDateTime dateUpdate;

    @PrePersist
    protected void onCreate() {
        dateCreate = LocalDateTime.now();
        dateUpdate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateUpdate = LocalDateTime.now();
    }
}