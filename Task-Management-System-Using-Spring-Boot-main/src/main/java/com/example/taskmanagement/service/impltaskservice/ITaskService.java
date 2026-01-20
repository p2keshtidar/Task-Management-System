package com.example.taskmanagement.service.impltaskservice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskmanagement.dto.TaskDTO;
import com.example.taskmanagement.dto.createtask.CreateTaskDTO;
import com.example.taskmanagement.dto.createtask.CreateTaskResponse;
import com.example.taskmanagement.dto.updatetask.UpdateTaskDTO;
import com.example.taskmanagement.dto.updatetask.UpdateTaskResponse;
import com.example.taskmanagement.enums.Status;
import com.example.taskmanagement.exceptionhandler.customexception.TaskNotFoundException;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.service.taskservice.TaskService;
import com.example.taskmanagement.service.utilities.UUIDConverter;

import lombok.extern.slf4j.Slf4j;

/**
 * implementation of the <strong> @TaskService </strong> interface for managing
 * tasks.
 * 
 * <p>
 * this service provides methods to <em>create, retrieve, update, and delete
 * tasks,</em> as well as to mark them as <em>completed</em>. It interacts with
 * the <strong> @TaskRepository </strong> to perform CRUD operations.
 * </p>
 */
@Service
@Slf4j
public class ITaskService implements TaskService {

	private static final Logger logger = LoggerFactory.getLogger(ITaskService.class);

	private TaskRepository taskRepository;

	/**
	 * @Constructor for @ITaskService
	 * 
	 * @param repository - the <strong> @TaskRepository </strong> to be used for
	 *                   task operations
	 */
	public ITaskService(TaskRepository repository) {
		this.taskRepository = repository;
	}

	/**
	 * Retrieve the list of tasks from the task repository based on the provided
	 * status. The method is expected to return a list of <strong> @Task </strong>
	 * objects. return the list of tasks that match the specified status to the
	 * caller.
	 */
	@Override
	public List<Task> getTasksByStatus(Status status) {
		logger.info("Fetching tasks with status: {} ", status);
		List<Task> tasks = taskRepository.findByStatus(status);
		logger.info("Found {} tasks with status: {} ", tasks.size(), status);
		return tasks;
	}

	/**
	 * fetch all tasks from the task repository, convert each <strong> @Task
	 * </strong> entity to <strong> @TaskDTO </strong>, and collect the results into
	 * a <strong> @List </strong> of <strong> @TaskDTO </strong> objects.
	 */
	@Override
	public List<TaskDTO> getAllTasks() {
		logger.info("Retrieving all tasks.");
		return taskRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	/**
	 * this method retrieves a task by its unique identifier <strong> @UUID
	 * </strong> after converting the provided string id. if the task is not found,
	 * it logs an error and throws a <strong> @TaskNotFoundException </strong>. if
	 * found, it converts the <strong> @Task </strong> entity to a <strong> @TaskDTO
	 * </strong> and returns it.
	 */
	@Override
	public Optional<TaskDTO> getTaskById(String id) {
		logger.info("Retrieving task with ID: {} ", id);
		UUID uuid = UUIDConverter.stringToUUIDConverter(id);
		if (!taskRepository.findById(uuid).isPresent()) {
			logger.error("Task Not Found with the given UUID: {} ", id);
			throw new TaskNotFoundException("Task Not Found with the given UUID : " + id);
		}
		return taskRepository.findById(uuid).map(this::convertToDTO);
	}

	/**
	 * this method creates a new <strong> @Task </strong> based on the provided
	 * <strong> @CreateTaskDTO </strong>, defaulting the due date to one week from
	 * now if not specified. It converts the DTO to a <strong> @Task </strong>
	 * entity, saves it to the repository, and logs the successful creation.
	 * Finally, it returns a <strong> @CreateTaskResponse </strong> containing the
	 * details of the newly created task.
	 */
	@Override
	@Transactional
	@Modifying
	public CreateTaskResponse createANewTask(CreateTaskDTO createTask) {
		logger.info("Creating a new task with details: {} ", createTask);
		LocalDate dueDate = (createTask.getDueDate() != null) ? createTask.getDueDate() : LocalDate.now().plusDays(7);
		Task task = convertToEntity(createTask);
		task.setDueDate(dueDate);
		Task savedTask = taskRepository.save(task);
		logger.info("Task created successfully with ID: {} ", savedTask.getId());
		return convertToCreateTaskResponse(savedTask);
	}

	/**
	 * this method updates a task identified by its <strong> @UUID </strong> using
	 * the provided <strong> @UpdateTaskDTO </strong>. It retrieves the existing
	 * task, applies the updates, saves the modified task to the repository, it
	 * returns a <strong> @UpdateTaskResponse </strong> containing the details of
	 * the updated task, logs the successful update. if the task is not found, it
	 * logs an error and throws a <strong> @TaskNotFoundException </strong>.
	 */
	@Override
	@Transactional
	@Modifying
	public UpdateTaskResponse updateAnExistingTask(String id, UpdateTaskDTO updateTask) {
		logger.info("Updating task with ID: {} ", id);
		UUID uuid = UUIDConverter.stringToUUIDConverter(id);
		return taskRepository.findById(uuid).map(existingTask -> {
			updateTaskFromDTO(existingTask, updateTask);
			Task updatedTask = taskRepository.save(existingTask);
			logger.info("Task updated successfully with ID: {} ", updatedTask.getId());
			return convertToUpdateTaskResponse(updatedTask);
		}).orElseThrow(() -> {
			logger.error("Task Not Found with the given UUID: {} ", id);
			return new TaskNotFoundException("Task Not Found with the given UUID: " + id);
		});
	}

	/**
	 * this method deletes a task identified by its <strong> @UUID </strong> after
	 * confirming its existence in the repository. if the task is not found, it logs
	 * an error and throws a <strong> @TaskNotFoundException </strong>. Upon
	 * successful deletion, it logs a confirmation message indicating the task has
	 * been deleted.
	 */
	@Override
	@Transactional
	@Modifying
	public void deleteAnExistingTask(String id) {
		logger.info("Deleting task with ID: {} ", id);
		UUID uuid = UUIDConverter.stringToUUIDConverter(id);
		if (!taskRepository.findById(uuid).isPresent()) {
			logger.error("Task Not Found with the given UUID: {} ", id);
			throw new TaskNotFoundException("Task Not Found with the given UUID : " + id);
		}
		taskRepository.deleteById(uuid);
		logger.info("Task deleted successfully with ID: {} ", id);
	}

	/**
	 * this method updates the status of a task identified by its
	 * 
	 * @UUID to <strong><em>"COMPLETED."</em></strong> It retrieves the task from
	 *       the repository, updates its status, and saves the changes while logging
	 *       the operation. if the task is not found, it logs an error and throws a
	 *       <strong> @TaskNotFoundException </strong>.
	 */
	@Override
	@Transactional
	@Modifying
	public TaskDTO markTaskAsCompleted(String id) {
		logger.info("Marking task with ID: {} as completed.", id);
		UUID uuid = UUIDConverter.stringToUUIDConverter(id);
		return taskRepository.findById(uuid).map(task -> {
			task.setStatus(Status.COMPLETED);
			Task updatedTask = taskRepository.save(task);
			logger.info("Task marked as completed with ID: {} ", updatedTask.getId());
			return convertToDTO(updatedTask);
		}).orElseThrow(() -> {
			logger.error("Task Not Found with the given UUID: {} ", id);
			return new TaskNotFoundException("Task Not Found with the given UUID: " + id);
		});
	}

	private Task convertToEntity(CreateTaskDTO createTaskDTO) {
		logger.debug("Converting CreateTaskRequestDTO to Task entity: {} ", createTaskDTO);
		Task task = new Task();
		task.setTitle(createTaskDTO.getTitle());
		task.setDescription(createTaskDTO.getDescription());
		return task;
	}

	private TaskDTO convertToDTO(Task task) {
		logger.debug("Converting Task entity to TaskDTO: {} ", task);
		return new TaskDTO(task.getId().toString(), task.getTitle(), task.getDescription(),
				task.getDueDate().toString(), task.getStatus().toString());
	}

	private CreateTaskResponse convertToCreateTaskResponse(Task task) {
		logger.debug("Converting Task entity to CreateTaskResponse: {} ", task);
		return new CreateTaskResponse(task.getId().toString(), task.getTitle(), task.getDescription(),
				task.getDueDate().toString(), task.getStatus().toString());
	}

	private Task updateTaskFromDTO(Task existingTask, UpdateTaskDTO updateTaskDTO) {
		logger.debug("Updating existing Task with new details: {} ", updateTaskDTO);
		if (updateTaskDTO.getTitle() != null && !updateTaskDTO.getTitle().isBlank()) {
			existingTask.setTitle(updateTaskDTO.getTitle());
		}
		if (updateTaskDTO.getDescription() != null && !updateTaskDTO.getDescription().isBlank()) {
			existingTask.setDescription(updateTaskDTO.getDescription());
		}
		if (updateTaskDTO.getDueDate() != null) {
			existingTask.setDueDate(updateTaskDTO.getDueDate());
		}
		return existingTask;
	}

	private UpdateTaskResponse convertToUpdateTaskResponse(Task task) {
		logger.debug("Converting Task entity to UpdateTaskResponse: {} ", task);
		return new UpdateTaskResponse(task.getId().toString(), task.getTitle(), task.getDescription(),
				task.getStatus().toString(), task.getDueDate().toString());
	}
}