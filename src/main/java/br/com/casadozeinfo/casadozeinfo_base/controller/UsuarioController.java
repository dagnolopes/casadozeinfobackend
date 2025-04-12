package br.com.casadozeinfo.casadozeinfo_base.controller;

import br.com.casadozeinfo.casadozeinfo_base.model.Usuario;
import br.com.casadozeinfo.casadozeinfo_base.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import br.com.casadozeinfo.casadozeinfo_base.service.TokenService;  // Importando TokenService

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final TokenService tokenService;  // Definindo o TokenService como dependência

    // Injeção de dependências
    @Autowired
    public UsuarioController(UsuarioService usuarioService, TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;  // Injetando o TokenService
    }

    @PostConstruct
    public void init() {
        System.out.println("TokenService injetado: " + (tokenService != null));
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarTodos();
    }

    // Exemplo de método que usa o TokenService
    @GetMapping("/validate-token")
    public String validateToken(String token) {
        String result = tokenService.validateToken(token);  // Usando o TokenService
        return result;
    }
}
