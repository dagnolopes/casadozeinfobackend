package br.com.casadozeinfo.casadozeinfo_base.service;

import br.com.casadozeinfo.casadozeinfo_base.model.Usuario;
import br.com.casadozeinfo.casadozeinfo_base.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}
