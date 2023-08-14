package mm.com.mytelpay.adapter.common.validator;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, PARAMETER, CONSTRUCTOR, METHOD, TYPE_USE})
@Documented
@Constraint(validatedBy = IdValidator.class)
public @interface ValidateUUID {
    String message() default "{Value is not invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
