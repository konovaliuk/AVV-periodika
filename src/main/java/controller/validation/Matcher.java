package controller.validation;

import java.util.Map;

public class Matcher {

    public static <T> Map<String, Boolean> match(T entity, MatchingBehavior<T> matchingBehavior) {
        return matchingBehavior.match(entity);
    }
}