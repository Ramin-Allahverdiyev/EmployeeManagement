package com.EmployeeManagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{5,}$", message = "Password must contain at least five characters, at least one number and both lower and uppercase letters and special characters")
@Constraint(validatedBy = { })
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidPassword {
    String message() default "Password must contain at least five characters, at least one number and both lower and uppercase letters and special characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
