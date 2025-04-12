package br.com.casadozeinfo.casadozeinfo_base.repository;

import br.com.casadozeinfo.casadozeinfo_base.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
