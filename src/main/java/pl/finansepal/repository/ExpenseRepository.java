package pl.finansepal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.finansepal.model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
