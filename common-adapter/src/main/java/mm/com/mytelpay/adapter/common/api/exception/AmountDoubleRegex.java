package mm.com.mytelpay.adapter.common.api.exception;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Documented
@Target({ElementType.FIELD})
@Constraint(validatedBy = AmountDoubleValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface AmountDoubleRegex {
    String message() default "The amount is not match regex";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class AmountDoubleValidator implements ConstraintValidator<AmountDoubleRegex, Double> {
    @Override
    public void initialize(AmountDoubleRegex contraint) {
        //  placeholder
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) throw new AmountInvalid();
        if (value < 1 || value > 99999999) throw new AmountInvalid();
        else return true;
    }
}
