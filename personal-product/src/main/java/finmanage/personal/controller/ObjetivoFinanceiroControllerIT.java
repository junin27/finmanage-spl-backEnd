package com.example.finmanage.pessoal.controller;

import com.example.finmanage.core.entity.Usuario;
import com.example.finmanage.core.repository.UsuarioRepository;
import com.example.finmanage.pessoal.dto.ObjetivoFinanceiroDTO;
import com.example.finmanage.pessoal.entity.ObjetivoFinanceiro;
import com.example.finmanage.pessoal.repository.ObjetivoFinanceiroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser; // Para simular usuário autenticado
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional; // Para rollback após teste

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // Carrega contexto da app Pessoal
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usar application-test.properties (ex: com H2)
@Transactional // Garante rollback das transações de teste
class ObjetivoFinanceiroControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjetivoFinanceiroRepository objetivoFinanceiroRepository;

    private Usuario testUser;

    @BeforeEach
    void setUp() {
        // Limpar repositórios se necessário ou confiar no @Transactional
        objetivoFinanceiroRepository.deleteAll();
        usuarioRepository.deleteAll(); // Cuidado se tiver dados que não quer apagar

        testUser = new Usuario();
        testUser.setUsername("testuser_pessoal_it");
        testUser.setPassword("password"); // Spring Security vai precisar disso no UserDetailsService mockado ou real
        testUser.setNomeCompleto("Test User IT");
        testUser.setEmail("testuser.pessoal.it@example.com");
        testUser = usuarioRepository.save(testUser);
    }

    @Test
    @WithMockUser(username = "testuser_pessoal_it") // Simula usuário logado
    void criarObjetivoFinanceiro_QuandoValido_RetornaCriado() throws Exception {
        ObjetivoFinanceiroDTO dto = new ObjetivoFinanceiroDTO();
        dto.setNome("Viagem para a Lua");
        dto.setValorAlvo(new BigDecimal("100000.00"));
        dto.setPrazo(LocalDate.now().plusYears(5));
        // dto.setUsuarioId(testUser.getId()); // O controller deve pegar do usuário autenticado

        mockMvc.perform(post("/api/pessoal/objetivos") // Endpoint do Produto A
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Viagem para a Lua"))
                .andExpect(jsonPath("$.valorAlvo").value(100000.00));

        // Verificar se foi salvo no banco
        assertEquals(1, objetivoFinanceiroRepository.findAll().size());
    }
}