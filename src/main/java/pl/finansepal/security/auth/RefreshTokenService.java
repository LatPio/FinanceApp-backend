package pl.finansepal.security.auth;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.finansepal.exeption.AppRuntimeException;
import pl.finansepal.repository.RefreshTokenRepository;
import pl.finansepal.security.model.RefreshToken;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshToken generateRefreshToken(String email){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        refreshToken.setEmail(email);

        return refreshTokenRepository.save(refreshToken);

    }

    void validateRefreshToken(String token){
        refreshTokenRepository.findByToken(token)
                .orElseThrow( ()-> new AppRuntimeException("Invalid Refresh Token"));
    }

    public void deleteRefreshToken (String token){
        refreshTokenRepository.deleteByToken(token);
    }

}
