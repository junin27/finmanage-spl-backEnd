package com.example.finmanage.core.service;

import com.example.finmanage.core.dto.CategoriaDTO;
import com.example.finmanage.core.entity.Categoria;
import com.example.finmanage.core.entity.Usuario;
import com.example.finmanage.core.enums.TipoCategoria;
import com.example.finmanage.core.repository.CategoriaRepository;
import com.example.finmanage.core.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void criarCategoria_ComUsuarioValido_DeveSalvarCategoria() {
        Long userId = 1L;
        Usuario mockUsuario = new Usuario();
        mockUsuario.setId(userId);

        CategoriaDTO dto = new CategoriaDTO();
        dto.setNome("Alimentação");
        dto.setTipo(TipoCategoria.DESPESA);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(mockUsuario));
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(invocation -> {
            Categoria cat = invocation.getArgument(0);
            cat.setId(10L); // Simula o ID gerado pelo banco
            return cat;
        });

        Categoria resultado = categoriaService.criarCategoria(dto, userId);

        assertNotNull(resultado);
        assertEquals("Alimentação", resultado.getNome());
        assertEquals(TipoCategoria.DESPESA, resultado.getTipo());
        assertEquals(userId, resultado.getUsuario().getId());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }
}