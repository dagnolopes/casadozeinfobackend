package br.com.casadozeinfo.casadozeinfo_base.service;

import br.com.casadozeinfo.casadozeinfo_base.dto.UsuarioDetailsDTO;
import br.com.casadozeinfo.casadozeinfo_base.model.RefreshToken;
import br.com.casadozeinfo.casadozeinfo_base.repository.RefreshTokenRepository;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service // Anotação que indica que a classe é um componente do Spring
public class TokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${api.security.token.secret}")
    private String secret;

    private final static int TEMPO_EXPIRACAO_HORAS = 1;
    private final static int TEMPO_EXPIRACAO_REFRESH_DIAS = 7;

    public String generateToken(UsuarioDetailsDTO usuarioModel) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(usuarioModel.getLoginusuario())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(TEMPO_EXPIRACAO_HORAS).toInstant(ZoneOffset.of("-03:00"));
    }

    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            Date expiration = decodedJWT.getExpiresAt();
            return expiration.before(new Date());
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token JWT inválido", e);
        }
    }

    public String generateRefreshToken(UsuarioDetailsDTO usuarioModel) {
    try {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String refreshToken = JWT.create()
                .withIssuer("auth-api")
                .withSubject(usuarioModel.getLoginusuario())
                .withExpiresAt(genRefreshTokenExpirationDate())
                .sign(algorithm);

        // Salvar no banco
        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setToken(refreshToken);
        tokenEntity.setUsuarioLogin(usuarioModel.getLoginusuario());
        tokenEntity.setDataExpiracao(genRefreshTokenExpirationDate());
        refreshTokenRepository.save(tokenEntity);

        return refreshToken;

    } catch (JWTCreationException exception) {
        throw new RuntimeException("Erro ao gerar o refresh token", exception);
    }
}

    public String generateRefreshTokenOLD_OK(UsuarioDetailsDTO usuarioModel) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(usuarioModel.getLoginusuario())
                    .withExpiresAt(genRefreshTokenExpirationDate())
                    // .withExpiresAt(LocalDateTime.now()
                    // .plusDays(TEMPO_EXPIRACAO_REFRESH_DIAS)
                    // .toInstant(ZoneOffset.of("-03:00")))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o refresh token", exception);
        }
    }

    private Instant genRefreshTokenExpirationDate() {
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("-03:00")); // Refresh token dura 7 dias
    }

}
