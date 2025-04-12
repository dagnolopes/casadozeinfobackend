package br.com.casadozeinfo.casadozeinfo_base.service;

import br.com.casadozeinfo.casadozeinfo_base.dto.TokenResponseDTO;
import br.com.casadozeinfo.casadozeinfo_base.dto.UsuarioDetailsDTO;
import br.com.casadozeinfo.casadozeinfo_base.model.RefreshToken;
import br.com.casadozeinfo.casadozeinfo_base.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public ResponseEntity<TokenResponseDTO> autenticar(UsuarioDetailsDTO usuario) {
        // Gera o access token
        String accessToken = tokenService.generateToken(usuario);

        // Remove tokens antigos para o mesmo usuário
        refreshTokenRepository.deleteByUsuarioLogin(usuario.getLoginusuario());

        // Cria novo refresh token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuarioLogin(usuario.getLoginusuario());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setDataExpiracao(Instant.now().plusSeconds(60 * 60 * 24 * 7)); // 7 dias

        // Salva no banco
        refreshTokenRepository.save(refreshToken);

        // Retorna os dois tokens
        // Map<String, String> response = new HashMap<>();
        // response.put("accessToken", accessToken);
        // response.put("refreshToken", refreshToken.getToken());

        TokenResponseDTO response = new TokenResponseDTO(accessToken, refreshToken.getToken());
        return ResponseEntity.ok(response);


        //return response;
    }
}
