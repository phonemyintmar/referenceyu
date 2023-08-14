package mm.com.mytelpay.adapter.common.api.exception;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Constraint(validatedBy = NullableNotBlankValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NullableNotBlank {
    String message() default "This field is not empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class NullableNotBlankValidator implements ConstraintValidator<NullableNotBlank, String> {
    @Override
    public void initialize(NullableNotBlank customConstarint) {
        // placeholder
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext cxt) {
        if (value == null) return false;
        return !value.trim().isEmpty();
    }
}
