package mm.com.mytelpay.adapter.common.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = PagingValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ValidatePaging {
    String message() default "Paging is not valid";

    String[] allowedSorts() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
