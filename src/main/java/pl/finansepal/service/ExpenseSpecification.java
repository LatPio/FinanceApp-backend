package pl.finansepal.service;

import jakarta.persistence.Cacheable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.finansepal.model.Expense;
import pl.finansepal.model.User;

@Component
public class ExpenseSpecification {

//     User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    public static  Specification<Expense> belongToUser(User user){

        return new Specification<Expense>() {
            @Override
            public Predicate toPredicate(Root<Expense> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("user"), user);
            }
        };
    }

}
