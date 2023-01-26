package pl.finansepal.repository;

import lombok.AllArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.finansepal.model.Expense;
import pl.finansepal.security.auth.AuthService;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {              //




}
