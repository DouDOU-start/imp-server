package cn.hanglok.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Allen
 * @description 弃用
 */
@Deprecated
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PatientSexValidator.class)
public @interface PatientSexValidation {
    String message() default "患者性别只能为F、M";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
