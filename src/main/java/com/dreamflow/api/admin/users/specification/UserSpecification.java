package com.dreamflow.api.admin.users.specification;

import com.dreamflow.api.auth.entity.Role;
import com.dreamflow.api.auth.entity.User;
import io.micrometer.core.instrument.search.Search;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jakarta.persistence.criteria.Predicate;

public class UserSpecification {
    public static Specification<User> filterUsers(Role role, String email, String username, LocalDate createdAfter, LocalDate createdBefore, LocalDate createdAt, SearchType searchType){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (role!=null){
                predicates.add(
                        criteriaBuilder.equal(root.get("role"), role)
                );
            }

            if (email != null && !email.isBlank()){
                buildSearchType("email", email, searchType, predicates, criteriaBuilder, root);
            }

            if (username != null && !username.isBlank()){
                buildSearchType("username", username, searchType, predicates, criteriaBuilder, root);
            }

            if (createdAfter != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                createdAfter
                        )
                );
            }

            if (createdBefore != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("createdAt"),
                                createdBefore
                        )
                );
            }

            if (createdAt != null) {

                LocalDateTime startOfDay = createdAt.atStartOfDay();
                LocalDateTime startOfNextDay = createdAt.plusDays(1).atStartOfDay();

                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                startOfDay
                        )
                );

                predicates.add(
                        criteriaBuilder.lessThan(
                                root.get("createdAt"),
                                startOfNextDay
                        )
                );
            }



            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void buildSearchType(String paramName, String param, SearchType searchType, List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<User> root){
        String value = param.toLowerCase(Locale.ROOT);
        switch (searchType) {
            case STARTS_WITH -> {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get(paramName)
                                ), value + "%"
                        )
                );
            }

            case ENDS_WITH -> {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get(paramName)
                                ), "%"+value
                        )
                );
            }

            case CONTAINS -> {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get(paramName)
                                ), "%"+value + "%"
                        )
                );
            }

            case EXACT -> {
                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(
                                        root.get(paramName)
                                ), value
                        )
                );
            }
        }
    }
}
