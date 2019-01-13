package controller.validation;

public class Validator {

    public static <T> ValidationData match(T entity, ValidationBehavior<T> validationBehavior) {
        return validationBehavior.match(entity);
    }
}