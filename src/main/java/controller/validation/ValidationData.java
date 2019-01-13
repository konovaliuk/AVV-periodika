package controller.validation;

import java.util.HashMap;
import java.util.Map;

public class ValidationData {
    private Map<String, String> errors;

    public ValidationData() {
        errors = new HashMap<>();
    }

    public void setError(String fieldName, String message) {
        errors.put(fieldName, message);
    }

    public void setError(String fieldName) {
        errors.put(fieldName, null);
    }

    public String getErrorMessage(String fieldName) {
        return errors.get(fieldName);
    }

    public boolean containsErrors() {
        return !errors.isEmpty();
    }

    public boolean isValid(String fieldName) {
        return !errors.containsKey(fieldName);
    }
}
