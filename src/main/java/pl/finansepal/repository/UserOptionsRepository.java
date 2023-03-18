package pl.finansepal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.finansepal.model.User;
import pl.finansepal.model.UserOptions;

import java.util.Optional;

public interface UserOptionsRepository extends JpaRepository<UserOptions, Long> {

    Optional<UserOptions> findByUser(User user);
    Optional<UserOptions> findByIdAndUser(Long aLong, User user);
}
