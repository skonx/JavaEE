package fr.trendev.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {MyMaxValidator.class})
public @interface MyMax {

    @Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(value = RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {

        public MyMax[] value();
    }

    public String message() default "{Max.message}";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

    public long value();
}
