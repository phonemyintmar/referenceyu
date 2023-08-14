package mm.com.mytelpay.adapter.common.validator;

import javax.validation.ConstraintValidatorContext;

public final class ValidatorUtils {
    private ValidatorUtils() {
        // default constructor
    }

    public static void createErrorField(
            ConstraintValidatorContext context,
            String field,
            String message,
            boolean disableDefaultConstraint) {
        if (disableDefaultConstraint) {
            context.disableDefaultConstraintViolation();
        }
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}
