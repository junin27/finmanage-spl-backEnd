package finmanage.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "financial_accounts")
@Getter
@Setter
public class FinancialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Ex: "Carteira", "Conta Corrente BB"

    private String typeAccount; // Ex: "CARTEIRA", "CONTA_CORRENTE", "POUPANCA"

    @Column(precision = 19, scale = 2)
    private BigDecimal initialBalance; // Saldo no momento da criação da conta

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "financialAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> transactions;
}
