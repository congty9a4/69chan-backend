package com.congty9a4.backend.repository.specification;

import com.congty9a4.backend.entity.Userchan;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

/**
 * JPA Specifications for User search using PostgreSQL Full Text Search
 * Provides type-safe, composable query building
 */
public class UserSpecification {



    private static final String TSQUERY = "to_tsquery";

    private static String preprocessQuery(String query) {
        // Basic preprocessing can be added here if needed
        if (query == null || query.trim().isEmpty()) {
            return null;
        }

        if (query.contains(" "))
            return query.replaceAll(" ", " | ");

        return query;
    }

    public static Specification<Userchan> searchByFTS(String _query) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            var query = preprocessQuery(_query);
            // Create the tsvector expression
            Expression<String> tsVector = criteriaBuilder.function(
                    "to_tsvector",
                    String.class,
                    criteriaBuilder.literal("english"),
                    criteriaBuilder.concat(
                            criteriaBuilder.coalesce(root.get("username"), ""),
                            criteriaBuilder.concat(
                                    criteriaBuilder.literal(" "),
                                    criteriaBuilder.coalesce(root.get("email"), "")
                            )
                    )
            );

            // Create the tsquery expression
            Expression<String> tsQuery = criteriaBuilder.function(
                    TSQUERY,
                    String.class,
                    criteriaBuilder.literal("english"),
                    criteriaBuilder.literal(query)
            );

            // Create the @@ operator using function
            Expression<Boolean> matches = criteriaBuilder.function(
                    "ts_match",
                    Boolean.class,
                    tsVector,
                    tsQuery
            );

            // Apply fetch only for non-count queries
            if (criteriaQuery.getResultType() != Long.class && criteriaQuery.getResultType() != long.class) {
                root.fetch("profile", JoinType.LEFT);
            }

            return criteriaBuilder.isTrue(matches);
        };
    }

    /**
     * Search users using PostgreSQL Full Text Search with websearch_to_tsquery
     * Supports advanced syntax: AND, OR, "exact phrase", -negation
     */

    /**
     * Order by relevance using ts_rank
     * Note: This needs to be applied separately using Sort or in the query
     */
    public static Specification<Userchan> orderByRelevance(String _query) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            var query = preprocessQuery(_query);
            if (query == null) {
                return criteriaBuilder.conjunction();
            }
            // Use the pre-calculated fts_document column
            Expression<String> tsVector = root.get("ftsDocument");

            // Create the tsquery expression
            Expression<String> tsQuery = criteriaBuilder.function(
                    TSQUERY,
                    String.class,
                    criteriaBuilder.literal("english"),
                    criteriaBuilder.literal(query)
            );

            // Create the ts_rank expression for ordering
            Expression<Double> rank = criteriaBuilder.function(
                    "ts_rank",
                    Double.class,
                    tsVector,
                    tsQuery
            );

            // Order by rank descending
            criteriaQuery.orderBy(criteriaBuilder.desc(rank));

            return criteriaBuilder.conjunction();
        };
    }

    /**
     * Fallback search using LIKE (for compatibility when FTS is not available)
     *
     * @param query The search query
     * @return Specification for LIKE-based search
     */
    public static Specification<Userchan> searchByLike(String query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (query == null || query.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String likePattern = "%" + query.toLowerCase() + "%";

            Predicate usernamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("username")),
                    likePattern
            );

            Predicate emailPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")),
                    likePattern
            );

            return criteriaBuilder.or(usernamePredicate, emailPredicate);
        };
    }

    /**
     * Filter by active status
     *
     * @param isActive Whether to filter for active users
     * @return Specification for active status filter
     */
    public static Specification<Userchan> isActive(Boolean isActive) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (isActive == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    /**
     * Filter by email domain
     *
     * @param domain The email domain to filter by
     * @return Specification for email domain filter
     */
    public static Specification<Userchan> hasEmailDomain(String domain) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (domain == null || domain.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("email"), "%" + domain);
        };
    }

    /**
     * Combine specifications with AND logic
     * Example: searchByFTS(query).and(isActive(true))
     */
    public static Specification<Userchan> and(Specification<Userchan>... specifications) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate[] predicates = new Predicate[specifications.length];
            for (int i = 0; i < specifications.length; i++) {
                predicates[i] = specifications[i].toPredicate(root, criteriaQuery, criteriaBuilder);
            }
            return criteriaBuilder.and(predicates);
        };
    }
}

