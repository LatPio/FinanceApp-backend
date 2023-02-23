package pl.finansepal.service.specification;

import jakarta.persistence.Cacheable;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.finansepal.model.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static pl.finansepal.service.specification.CommonSpecification.searchCriteriaList;

@Component
public class ExpenseSpecification implements Specification<Expense>  {  //Specification<Expense>

    private List<SearchCriteria> searchCriteriaList;


    public ExpenseSpecification() {
        this.searchCriteriaList = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        searchCriteriaList.add(criteria);
    }

    public static Specification<Expense> belongToUser(User user) {

        return new Specification<Expense>() {
            @Override
            public Predicate toPredicate(Root<Expense> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("user"), user);
            }
        };
    }

    @Override
    public Predicate toPredicate(Root<Expense> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : searchCriteriaList) {
            if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
                predicates.add(
                        criteriaBuilder.greaterThan(
                                root.get(criteria.getKey()),
                                criteria.getValue().toString())

                );
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
                predicates.add(
                        criteriaBuilder.lessThan(
                                root.get(criteria.getKey()),
                                criteria.getValue().toString())

                );
            } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get(criteria.getKey()),
                                criteria.getValue().toString())

                );
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get(criteria.getKey()),
                                criteria.getValue().toString())

                );
            } else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
                predicates.add(
                        criteriaBuilder.notEqual(
                                root.get(criteria.getKey()),
                                criteria.getValue().toString())

                );
            } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(criteria.getKey()),
                                criteria.getValue().toString())

                );
            } else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(criteria.getKey())),
                                 "%" + criteria.getValue().toString().toLowerCase() + "%")

                );
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(criteria.getKey())),
                                criteria.getValue().toString().toLowerCase() + "%" )

                );
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_START)) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(criteria.getKey())),
                                "%" + criteria.getValue().toString().toLowerCase())

                );
            } else if (criteria.getOperation().equals(SearchOperation.IN)) {
                predicates.add(
                        criteriaBuilder.in(
                                root.get(criteria.getKey())).
                                value(criteria.getValue())

                );
            } else if (criteria.getOperation().equals(SearchOperation.NOT_IN)) {
                predicates.add(
                        criteriaBuilder.not(
                                root.get(criteria.getKey())).in(criteria.getValue())

                );
            } else if (criteria.getOperation().equals(SearchOperation.TAG_OBJECT)) {

                query.distinct(true);
                Subquery<Tag> tagSubQuery = query.subquery(Tag.class);
                Root<Tag> tagRoot= tagSubQuery.from(Tag.class);
                Expression<Collection<Expense>> tagExpense = tagRoot.get("expenses");
                tagSubQuery.select(tagRoot);
                predicates.add(
                        criteriaBuilder.exists(
                                tagSubQuery.where(
                                        criteriaBuilder.equal(tagRoot.get("id"), criteria.getValue()),
                                        criteriaBuilder.isMember(root, tagExpense)
                                )
                        )
                );

            } else if (criteria.getOperation().equals(SearchOperation.DATE_GREATER_THAN_EQUAL)) {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get(criteria.getKey()), LocalDateTime.parse(criteria.getValue().toString(), inputFormatter))

                );

            } else if (criteria.getOperation().equals(SearchOperation.DATE_LESS_THAN_EQUAL)) {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get(criteria.getKey()), LocalDateTime.parse(criteria.getValue().toString(), inputFormatter))

                );

            }
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

    }

}
