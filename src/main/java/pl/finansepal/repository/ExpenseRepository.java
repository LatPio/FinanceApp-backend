package pl.finansepal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.finansepal.model.Expense;
import pl.finansepal.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {              //


    @Override
    Optional<Expense> findById(Long aLong);

    Optional<Expense> findByIdAndUser(Long id, User user);

    Optional<Void> deleteByIdAndUser(Long id, User user);

    @Query("select sum(e.amount) from Expense e where e.date between ?1 and ?2 and e.user = ?3")
    BigDecimal sumOfExpenseByDate(LocalDateTime startDate, LocalDateTime endDate, User user);

    @Query("select sum(i.amount) from Expense i inner join i.tags tags where tags.id = ?1 and i.date between ?2 and ?3 and i.user = ?4")
    BigDecimal sumByTags_IdAndDateBetween(Long id, LocalDateTime dateStart, LocalDateTime dateEnd, User user);

}
