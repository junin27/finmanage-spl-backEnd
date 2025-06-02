package finmanage.core.entity;

import finmanage.core.enums.CategoryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categorias")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Ex: "Alimentação", "Salário", "Transporte"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType typeCategory; // RECEITA ou DESPESA

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Categorias são por usuário
    private User usuario;
}