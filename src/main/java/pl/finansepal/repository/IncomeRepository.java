package pl.finansepal.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.finansepal.model.Income;
import pl.finansepal.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> , JpaSpecificationExecutor<Income>{

    @Override
    Optional<Income> findById(Long aLong);

    Optional<Income> findByIdAndUser(Long id, User user);

    Optional<Void> deleteByIdAndUser(Long id, User user);


    @Query(value = "select sum(i.amount) from Income i where i.date between ?1 and ?2 and i.user = ?3")
    BigDecimal sumOfIncomesByDate(LocalDateTime startDate, LocalDateTime endDate, User user);

    @Query("select sum(i.amount) from Income i inner join i.tags tags where tags.id = ?1 and i.date between ?2 and ?3 and i.user = ?4")
    BigDecimal sumByTags_IdAndDateBetween(Long id, LocalDateTime dateStart, LocalDateTime dateEnd, User user);

    @Query("select min(i.date) from Income i where i.user = ?1")
    LocalDateTime findFirstDate(User user);


}
