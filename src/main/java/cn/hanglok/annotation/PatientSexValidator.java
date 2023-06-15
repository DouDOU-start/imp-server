package cn.hanglok.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

/**
 * @author Allen
 * @version 1.0
 * @className MyCustomValidator
 * @description 弃用
 * @date 2023/6/13 17:14
 */
@Deprecated
public class PatientSexValidator implements ConstraintValidator<PatientSexValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        List<String> sex = Arrays.asList("F", "M");
        return value == null || sex.contains(value);
    }
}
