package br.com.casadozeinfo.casadozeinfo_base.repository;

import br.com.casadozeinfo.casadozeinfo_base.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUsuarioLogin(String usuarioLogin);
}
