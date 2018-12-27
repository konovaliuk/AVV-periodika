package controller.validation;

import java.util.Map;

public interface MatchingBehavior<T> {
    Map<String, Boolean> match(T object);
}
