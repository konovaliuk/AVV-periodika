package controller.validation;

public interface ValidationBehavior<T> {
    ValidationData match(T object);
}
