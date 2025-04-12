package br.com.casadozeinfo.casadozeinfo_base.controller;

import br.com.casadozeinfo.casadozeinfo_base.dto.AccessTokenDTO;
import br.com.casadozeinfo.casadozeinfo_base.dto.RefreshTokenRequestDTO;
import br.com.casadozeinfo.casadozeinfo_base.dto.TokenResponseDTO;
import br.com.casadozeinfo.casadozeinfo_base.dto.UsuarioDetailsDTO;
import br.com.casadozeinfo.casadozeinfo_base.model.RefreshToken;
import br.com.casadozeinfo.casadozeinfo_base.repository.RefreshTokenRepository;
import br.com.casadozeinfo.casadozeinfo_base.service.AuthService;
import br.com.casadozeinfo.casadozeinfo_base.service.TokenService;
import br.com.casadozeinfo.casadozeinfo_base.dto.ErrorResponseDTO;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody UsuarioDetailsDTO usuario) {
        // ResponseEntity<TokenResponseDTO> tokens = authService.autenticar(usuario);
        // return ResponseEntity.ok(tokens);
        return authService.autenticar(usuario);
    }

    @PostMapping("/login-okok")
    public ResponseEntity<Map<String, String>> login_okok(@RequestBody UsuarioDetailsDTO usuario) {
        // Gera access token
        String accessToken = tokenService.generateToken(usuario);

        // Remove refresh tokens antigos do usuário
        refreshTokenRepository.deleteByUsuarioLogin(usuario.getLoginusuario());

        // Cria novo refresh token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuarioLogin(usuario.getLoginusuario());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setDataExpiracao(Instant.now().plusSeconds(60 * 60 * 24 * 7)); // 7 dias

        refreshTokenRepository.save(refreshToken);

        // Retorna como JSON
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken.getToken());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login-ok")
    public ResponseEntity<?> login_ok(@RequestBody UsuarioDetailsDTO usuario) {
        // Aqui deveria validar usuário e senha com o banco, mas vamos supor que está ok
        String accessToken = tokenService.generateToken(usuario);
        String refreshToken = tokenService.generateRefreshToken(usuario);

        return ResponseEntity.ok().body(
                java.util.Map.of(
                        "access_token", accessToken,
                        "refresh_token", refreshToken));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AccessTokenDTO> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        String refreshToken = request.getRefreshToken();
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        if (token.getDataExpiracao().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            return ResponseEntity.status(401).build();
        }

        String novoAccessToken = tokenService.generateToken(new UsuarioDetailsDTO(token.getUsuarioLogin()));
        return ResponseEntity.ok(new AccessTokenDTO(novoAccessToken));
    }

    // @PostMapping("/refresh-token")
    // public ResponseEntity<String> refreshToken(@RequestBody Map<String, String>
    // request) {
    // String refreshToken = request.get("refresh_token");
    // RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
    // .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

    // if (token.getDataExpiracao().isBefore(Instant.now())) {
    // refreshTokenRepository.delete(token); // opcional: limpeza automática
    // return ResponseEntity.status(401).body("Refresh token expirado");
    // }

    // String usuario = token.getUsuarioLogin();
    // UsuarioDetailsDTO usuarioDTO = new UsuarioDetailsDTO();
    // usuarioDTO.setLoginusuario(usuario);
    // String novoAccessToken = tokenService.generateToken(usuarioDTO);

    // return ResponseEntity.ok(novoAccessToken);
    // }

    @DeleteMapping("/logout/{usuarioLogin}")
    public ResponseEntity<Void> logout(@PathVariable String usuarioLogin) {
        refreshTokenRepository.deleteByUsuarioLogin(usuarioLogin);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh-token-ok")
    public ResponseEntity<?> refreshToken_ok(@RequestBody java.util.Map<String, String> body) {
        String refreshToken = body.get("refresh_token");

        String usuarioLogin = tokenService.validateToken(refreshToken);

        if (usuarioLogin == null || usuarioLogin.isEmpty()) {
            return ResponseEntity.status(401).body("Refresh token inválido ou expirado.");
        }

        UsuarioDetailsDTO usuario = new UsuarioDetailsDTO();
        usuario.setLoginusuario(usuarioLogin);

        String novoAccessToken = tokenService.generateToken(usuario);

        return ResponseEntity.ok().body(
                java.util.Map.of("access_token", novoAccessToken));
    }

    @PostMapping("/validate-refresh-token-old")
    public ResponseEntity<String> validateRefreshToken_olj(@RequestBody String refreshToken) {
        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByToken(refreshToken.trim());

        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token inválido");
        }

        RefreshToken token = tokenOptional.get();
        if (token.getDataExpiracao().isBefore(Instant.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expirado");
        }

        // Gerar novo access token
        String novoAccessToken = tokenService.generateToken(new UsuarioDetailsDTO(token.getUsuarioLogin()));
        return ResponseEntity.ok(novoAccessToken);
    }

    @PostMapping("/validate-refresh-token")
    public ResponseEntity<?> validateRefreshToken(@RequestBody RefreshTokenRequestDTO request) {
        String refreshToken = request.getRefreshToken().trim();

        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByToken(refreshToken);

        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDTO("Refresh token inválido"));
        }

        RefreshToken token = tokenOptional.get();
        if (token.getDataExpiracao().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDTO("Refresh token expirado"));
        }

        String novoAccessToken = tokenService.generateToken(new UsuarioDetailsDTO(token.getUsuarioLogin()));
        return ResponseEntity.ok(new AccessTokenDTO(novoAccessToken));
    }

}
