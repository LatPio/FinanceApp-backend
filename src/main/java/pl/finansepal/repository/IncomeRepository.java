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

    @Query("select sum(amount) from Income where date between ?1 and ?2")
    BigDecimal sumOfIncomesByDate(LocalDateTime startDate, LocalDateTime endDate, Specification<Income> incomeSpecification);

    @Query("select sum(i.amount) from Income i inner join i.tags tags where tags.id = ?1 and i.date between ?2 and ?3")
    BigDecimal sumByTags_IdAndDateBetween(Long id, LocalDateTime dateStart, LocalDateTime dateEnd, Specification<Income> incomeSpecification);

    @Query("select min(i.date) from Income i")
    LocalDateTime findFirstDate(Specification<Income> incomeSpecification);


}
