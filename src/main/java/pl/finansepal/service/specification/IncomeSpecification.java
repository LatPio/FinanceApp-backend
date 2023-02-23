package pl.finansepal.service.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.finansepal.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class IncomeSpecification implements Specification<Income>{

    private List<SearchCriteria> searchCriteriaList;

    public IncomeSpecification() {
        this.searchCriteriaList = new ArrayList<>();
    }
    public void add(SearchCriteria criteria) {
        searchCriteriaList.add(criteria);
    }
    public static Specification<Income> belongToUser(User user){
            return new Specification<Income>() {
                @Override
                public Predicate toPredicate(Root<Income> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.equal(root.get("user"), user);
                }
            };
        }

    @Override
    public Predicate toPredicate(Root<Income> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

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

            }else if (criteria.getOperation().equals(SearchOperation.TAG_OBJECT)) {

                query.distinct(true);
                Subquery<Tag> tagSubQuery = query.subquery(Tag.class);
                Root<Tag> tagRoot= tagSubQuery.from(Tag.class);
                Expression<Collection<Income>> tagIncome = tagRoot.get("incomes");
                tagSubQuery.select(tagRoot);
                predicates.add(
                        criteriaBuilder.exists(
                                tagSubQuery.where(
                                criteriaBuilder.equal(tagRoot.get("id"), criteria.getValue()),
                                criteriaBuilder.isMember(root, tagIncome)
                                )
                        )
                );

            }
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

    }


//        public static Specification<Income> findById(Long id){
//          return new Specification<Income>() {
//              @Override
//              public Predicate toPredicate(Root<Income> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                  return criteriaBuilder.equal(root.get("id"), id);
//              }
//          };
//        }
}
