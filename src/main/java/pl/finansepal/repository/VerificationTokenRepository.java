package pl.finansepal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.finansepal.model.VerificationToken;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository  extends JpaRepository<VerificationToken, Long>
{

    Optional<VerificationToken> findByToken(String token);
}
