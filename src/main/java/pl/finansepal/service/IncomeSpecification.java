package pl.finansepal.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import pl.finansepal.model.Income;
import pl.finansepal.model.User;

public class IncomeSpecification {

        public static Specification<Income> belongToUser(User user){
            return new Specification<Income>() {
                @Override
                public Predicate toPredicate(Root<Income> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get("user"), user);
                }
            };
        }
}
