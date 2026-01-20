package com.example.taskmanagement.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.dto.createtask.CreateTaskDTO;
import com.example.taskmanagement.dto.createtask.CreateTaskResponse;
import com.example.taskmanagement.dto.updatetask.UpdateTaskDTO;
import com.example.taskmanagement.dto.updatetask.UpdateTaskResponse;
import com.example.taskmanagement.service.taskservice.TaskService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @TaskController
 * 
 *                 this class serves as the REST controller for managing tasks
 *                 in the application. It provides endpoints to perform CRUD
 *                 (create, read, update, delete) operations on tasks, as well
 *                 as additional features like marking tasks as completed.
 * 
 *                 Responsibilities: - handle incoming http requests related to
 *                 task management. - delegate business logic to
 *                 the @TaskService layer. - Ensure appropriate http responses
 *                 are returned to clients. - validate input data and handle
 *                 errors gracefully.
 * 
 *                 Design Rationale: - Separation of concerns: The controller
 *                 focuses solely on handling http requests and responses,
 *                 delegating the core business logic to the service layer. -
 *                 Adherence to REST principles: each endpoint corresponds to a
 *                 specific operation on the task resource, using appropriate
 *                 http methods and status codes. - Scalability: The modular
 *                 design allows the controller to remain lightweight, making it
 *                 easier to extend functionality without cluttering the class.
 * 
 *                 key Endpoints: - GET /v1/tasks: Retrieve all tasks. - GET
 *                 /v1/tasks/{id}: Retrieve a specific task by id. - POST
 *                 /v1/tasks: Create a new task. - PUT /v1/tasks/{id}: update an
 *                 existing task. - DELETE /v1/tasks/{id}: delete a task. -
 *                 PATCH /v1/tasks/{id}/complete: mark a task as complete.
 * 
 *                 this class ensures a clean API interface for interacting with
 *                 the task management system, supporting both usability and
 *                 maintainability.
 */

@Slf4j
@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	private TaskService taskService;

	/**
	 * @Constructor for <strong> @TaskController </strong>
	 * 
	 * @param service - the <strong> @TaskService </strong> to be used for task
	 *                operations
	 */
	public TaskController(TaskService service) {
		this.taskService = service;
	}

	/**
	 * Retrieve all tasks.
	 *
	 * @return a list of <strong> @TaskDTO </strong> representing all tasks if
	 *         found. otherwise a message <strong> "No tasks found, please create a
	 *         few tasks" </strong> will be sent to the client.
	 */
	@GetMapping
	public ResponseEntity<Object> getAllTasks() {
		logger.info("Fetching all tasks");
		List<TaskDTO> tasks = taskService.getAllTasks();
		if (tasks.isEmpty()) {
			Map<String, String> response = Map.of("message", "No tasks found, please create a few tasks.");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}

	/**
	 * Retrieve a task by its id
	 *
	 * @param id - the <strong> id </strong> of the task to retrieve.
	 * @return <strong> @ResponseEntity </strong> containing the <strong> @TaskDTO
	 *         </strong> if found, otherwise a <strong> @HttpStatus.NOT_FOUND
	 *         </strong> status.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<TaskDTO> getTaskById(@PathVariable String id) {
		logger.info("Fetching task with ID: {} ", id);
		return taskService.getTaskById(id).map(ResponseEntity::ok).orElseGet(() -> {
			logger.warn("Task with ID: {} not found ", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		});
	}

	/**
	 * Create a new task.
	 *
	 * @param @CreateTaskDTO the task data to create.
	 * @return <strong> @ResponseEntity </strong> containing the
	 *         <strong> @CreateTaskResponse </strong> created task response.
	 */
	@PostMapping
	public ResponseEntity<CreateTaskResponse> createTask(@Valid @RequestBody CreateTaskDTO createTaskDTO) {
		logger.info("Creating a new task with details: {} ", createTaskDTO);
		CreateTaskResponse response = taskService.createANewTask(createTaskDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * Delete a task by its id
	 *
	 * @param id - the <strong> id </strong> of the task to delete.
	 * @return <strong> @ResponseEntity </strong> containing a
	 *         <strong> @HttpStatus.NO_CONTENT </strong> message.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> deleteTask(@PathVariable String id) {
		logger.info("Deleting task with ID: {} ", id);
		taskService.deleteAnExistingTask(id);
		Map<String, String> response = Map.of("Message", "Task has been Deleted Successfully.", "id", id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
	}

	/**
	 * mark a task as completed.
	 *
	 * @param id - the <strong> id </strong> of the task to mark as completed.
	 * @return <strong> @ResponseEntity </strong> containing a success
	 *         <strong> @HttpStatus.OK </strong> message and the completed task.
	 */
	@PatchMapping("/{id}/complete")
	public ResponseEntity<Map<String, Object>> markTaskAsComplete(@PathVariable String id) {
		logger.info("Marking task with ID: {} as completed", id);
		TaskDTO dto = taskService.markTaskAsCompleted(id);
		Map<String, Object> response = Map.of("Message", "Task has been Marked as Completed Successfully.", "task",
				dto);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * update an existing task.
	 *
	 * @param id             - the <strong> id </strong> of the task to update.
	 * @param @UpdateTaskDTO to update the existing task data.
	 * @return <strong> @ResponseEntity </strong> containing the
	 *         <strong> @UpdateTaskResponse </strong> updated task response.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<UpdateTaskResponse> updateTask(@PathVariable String id,
			@Valid @RequestBody UpdateTaskDTO updateTaskDTO) {
		logger.info("Updating task with ID: {} with new details: {} ", id, updateTaskDTO);
		return ResponseEntity.ok(taskService.updateAnExistingTask(id, updateTaskDTO));
	}
}