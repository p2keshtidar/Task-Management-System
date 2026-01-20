package com.example.taskmanagement.customvalidationannotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom annotation for validating that at least one fields in a class is
 * non-null.
 * 
 * <p>
 * this annotation is used to enforce the rule that a request DTO must have at
 * least one fields populated when performing certain operations, such as
 * updates.
 * 
 * <p>
 * It works by associating this annotation with the
 * {@link @AtLeastOneFieldValidator} class, which contains the logic to check if
 * the validation passes.
 * 
 * example usage:
 * 
 * <pre>
 * {@code
 * @AtLeastOneFieldNotNull(message = "At least one field must be Provided to Update.")
 * public class UpdateTaskRequestDTO {
 * 	private String title;
 * 	private String description;
 * 	private LocalDate dueDate;
 * }
 * </pre>
 * 
 * attributes:
 * <ul>
 * <li>{@code message} - Custom error message to be displayed when validation
 * fails.</li>
 * <li>{@code groups} - Allows grouping constraints together for different
 * validation contexts.</li>
 * <li>{@code payload} - Provides additional metadata for validation failure
 * cases.</li>
 * </ul>
 * 
 * <p>
 * Can be used in any DTO or entity where at least one field must be non-null
 * for processing.
 */
@Documented
@Constraint(validatedBy = AtLeastOneFieldValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneFieldNotNull {

	String message() default "At Least one field must be provided to Update.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}