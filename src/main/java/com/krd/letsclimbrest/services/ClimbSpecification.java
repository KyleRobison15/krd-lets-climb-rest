package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.entities.Climb;
import com.krd.letsclimbrest.entities.User;
import com.krd.letsclimbrest.exception.InvalidFilterException;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.Map;

public class ClimbSpecification {

    public static Specification<Climb> byUserUsername(String username) {
        return (root, query, criteriaBuilder) -> {
            Join<Climb, User> join = root.join("user");
            return criteriaBuilder.equal(join.get("username"), username);
        };
    }

    public static Specification<Climb> gradeLike(String grade) {

        // Check for overall grade category
        if (grade.equals("5.4") || grade.equals("5.5") || grade.equals("5.6") || grade.equals("5.7") ||
            grade.equals("5.8") || grade.equals("5.9") || grade.equals("5.10") || grade.equals("5.11") ||
            grade.equals("5.12") || grade.equals("5.13") || grade.equals("5.14") || grade.equals("5.15")) {

            // Return specification to find climbs where grade has the grade category
            // For example, when the request wants to filter climbs by grade=5.10, we want to return all climbs in the 5.10 category
            // This includes, 5.10-, 5.10+, 5.10a, 5.10b, 5.10c and 5.10d

            return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("grade"), grade + "%");
        }

        // If the grade filter requested is more specific (ie. 5.10a) return ONLY 5.10a grade climbs
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("grade"), "%" + grade + "%");
    }

    public static Specification<Climb> boulderGradeLike(String boulderGrade) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("grade"), "%" + boulderGrade + "%");
    }

    public static Specification<Climb> styleLike(String style) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("style"), "%" + style + "%");
    }

    public static Specification<Climb> pitches(String expression) {

        Integer num = parseExpression(expression, "pitches");

        if (expression.contains(">=")) {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("pitches"), num));
        }

        if (expression.contains("<=")) {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("pitches"), num));
        }

        if (expression.contains(">")) {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("pitches"), num));
        }

        if (expression.contains("<")) {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("pitches"), num));
        }

        // Return default condition of equal
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("pitches"), num));
    }

    public static Specification<Climb> dangerLike(String danger) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("danger"), "%" + danger + "%");
    }

    public static Specification<Climb> stateAbbreviationLike(String stateAbbreviation) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("stateAbbreviation"), "%" + stateAbbreviation + "%");
    }

    public static Specification<Climb> areaNameLike(String areaName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("areaName"), "%" + areaName + "%");
    }

    public static Specification<Climb> cragNameLike(String cragName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("cragName"), "%" + cragName + "%");
    }

    public static Specification<Climb> tickedIs(Boolean isTicked) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isTicked"), isTicked);
    }

    public static Specification<Climb> starsEqual(String expression) {
        Integer num = parseExpression(expression, "stars");

        if (expression.contains(">=")) {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("stars"), num));
        }

        if (expression.contains("<=")) {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("stars"), num));
        }

        if (expression.contains(">")) {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("stars"), num));
        }

        if (expression.contains("<")) {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("stars"), num));
        }

        // Return default condition of equal
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("stars"), num));
    }

    private static Integer parseExpression(String expression, String property) {

        if (expression.contains(">=")) {
            return Integer.parseInt(expression.trim().replace(" ", "").replace(">=", ""));
        }

        if (expression.contains("<=")) {
            return Integer.parseInt(expression.trim().replace(" ", "").replace("<=", ""));
        }

        if (expression.contains(">")) {
            return Integer.parseInt(expression.trim().replace(" ", "").replace(">", ""));
        }

        if (expression.contains("<")) {
            return Integer.parseInt(expression.trim().replace(" ", "").replace("<", ""));
        }

        if (expression.contains("=")) {
            return Integer.parseInt(expression.trim().replace(" ", "").replace("=", ""));
        }

        int parsedNum;

        try {
            parsedNum = Integer.parseInt(expression);
        } catch (Exception e) {
            Map<String, String> exceptionDetails = new HashMap<>();
            exceptionDetails.put(property, "Filter expressions can only be >, <, >=, or <=");
            throw new InvalidFilterException("INVALID_FILTER_EXPRESSION", exceptionDetails, property);
        }

        return parsedNum;

    }

}
