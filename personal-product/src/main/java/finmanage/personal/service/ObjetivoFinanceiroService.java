package com.example.finmanage.pessoal.service;

import com.example.finmanage.core.entity.Usuario;
import com.example.finmanage.core.repository.UsuarioRepository; // Do core
import com.example.finmanage.pessoal.entity.ObjetivoFinanceiro;
import com.example.finmanage.pessoal.repository.ObjetivoFinanceiroRepository; // Do produto
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ObjetivoFinanceiroService {

    private final ObjetivoFinanceiroRepository objetivoRepository;
    private final UsuarioRepository usuarioRepository; // Injetando repo do core

    @Autowired
    public ObjetivoFinanceiroService(ObjetivoFinanceiroRepository objetivoRepository,
                                     UsuarioRepository usuarioRepository) {
        this.objetivoRepository = objetivoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ObjetivoFinanceiro criarObjetivo(ObjetivoFinanceiro objetivo, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        objetivo.setUsuario(usuario);
        return objetivoRepository.save(objetivo);
    }
    // ... outros métodos
}