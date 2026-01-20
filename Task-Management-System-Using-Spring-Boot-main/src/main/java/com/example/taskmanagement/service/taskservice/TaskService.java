package com.example.taskmanagement.service.taskservice;

import java.util.List;
import java.util.Optional;

import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.dto.createtask.CreateTaskDTO;
import com.example.taskmanagement.dto.createtask.CreateTaskResponse;
import com.example.taskmanagement.dto.updatetask.UpdateTaskDTO;
import com.example.taskmanagement.dto.updatetask.UpdateTaskResponse;
import com.example.taskmanagement.enums.Status;
import com.example.taskmanagement.model.Task;

/**
 * <p>
 * service interface for managing tasks.
 * </p>
 * <p>
 * this interface defines the operations that can be performed on tasks,
 * including <em>retrieving, creating, updating, and deleting tasks.</em> It
 * also includes a method to mark a task as <em>completed.</em>
 * </p>
 */
public interface TaskService {

	/**
	 * Retrieves all tasks.
	 * 
	 * @return a list of <strong> @TaskDTO </strong> objects representing all tasks
	 */
	abstract List<TaskDTO> getAllTasks();

	/**
	 * Retrieves a task by its id.
	 * 
	 * @param id - the <strong> id </strong> of the task to retrieve
	 * @return an <strong> @Optional </strong> containing the <strong> @TaskDTO
	 *         </strong> if found, or empty if not
	 */
	abstract Optional<TaskDTO> getTaskById(String id);

	/**
	 * Creates a new task.
	 * 
	 * @param @CreateTaskDTO - the details of the new task to create
	 * @return a <strong> @CreateTaskResponse </strong> response <strong> @Record
	 *         </strong> object containing details of the created task
	 */
	abstract CreateTaskResponse createANewTask(CreateTaskDTO createTask);

	/**
	 * Updates an existing task.
	 * 
	 * @param id             - the <strong> id </strong> of the task to update
	 * @param @UpdateTaskDTO the updated task details
	 * @return a <strong> @UpdateTaskResponse </strong> response <strong> @Record
	 *         </strong> object containing details of the updated task
	 */
	abstract UpdateTaskResponse updateAnExistingTask(String id, UpdateTaskDTO updateTask);

	/**
	 * Deletes an existing task by its id.
	 * 
	 * @param id - the <strong> id </strong> of the task to delete
	 */
	abstract void deleteAnExistingTask(String id);

	/**
	 * Marks a task as completed.
	 * 
	 * @param id - the <strong> id </strong> of the task to mark as completed
	 * @return the updated <strong> @TaskDTO </strong> representing the completed
	 *         task
	 */
	abstract TaskDTO markTaskAsCompleted(String id);

	/**
	 * Get a <strong> @Task </strong> by <strong> @Status </strong>
	 * 
	 * @param status - the status of the task
	 * @return a <strong> @List </strong> of <strong> @Task </strong> tasks
	 */
	abstract List<Task> getTasksByStatus(Status status);
}