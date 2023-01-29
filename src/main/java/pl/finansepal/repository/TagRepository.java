package pl.finansepal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.finansepal.model.Tag;
import pl.finansepal.model.User;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long > , JpaSpecificationExecutor<Tag> {



    Optional<Tag> findByIdAndUser(Long id, User user);

    Optional<Void>  deleteByIdAndUser(Long id, User user);
}
