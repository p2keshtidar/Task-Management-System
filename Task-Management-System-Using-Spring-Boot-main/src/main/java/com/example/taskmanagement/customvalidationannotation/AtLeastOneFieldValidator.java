package com.example.taskmanagement.customvalidationannotation;

import com.example.taskmanagement.dto.updatetask.UpdateTaskDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator implementation for checking if at least one field in a DTO
 * is non-null.
 * 
 * <p>
 * this class is used in conjunction with the <strong> @AtLeastOneFieldNotNull
 * </strong> annotation to ensure that at least one fields in the target object
 * is populated with a non-null value.
 * 
 * <p>
 * The primary use case is for update operations where partial updates are
 * allowed, but sending an entirely empty request body should not be valid.
 * 
 * <p>
 * How it works:
 * <ul>
 * <li>Scans all the fields of the annotated class.</li>
 * <li>Returns <strong> {@code true} </strong> if at least one field is
 * non-null.</li>
 * <li>Returns <strong> {@code false} </strong> if all fields are null.</li>
 * </ul>
 * 
 * <p>
 * this validator is flexible and can be reused for other classes where such
 * validation logic is needed.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * {@code
 * @AtLeastOneFieldNotNull(message = "At least one field must be provided to update.")
 * public class UpdateTaskDTO {
 * 	private String title;
 * 	private String description;
 * 	private LocalDate dueDate;
 * }
 * </pre>
 */
public class AtLeastOneFieldValidator implements ConstraintValidator<AtLeastOneFieldNotNull, UpdateTaskDTO> {

	@Override
	public boolean isValid(UpdateTaskDTO value, ConstraintValidatorContext context) {
		return value.getTitle() != null || value.getDescription() != null || value.getDueDate() != null;
	}
}