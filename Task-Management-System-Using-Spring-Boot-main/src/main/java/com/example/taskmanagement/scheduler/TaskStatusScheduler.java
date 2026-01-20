package com.example.taskmanagement.scheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskmanagement.enums.Status;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.service.taskservice.TaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Service for scheduling and updating the status of tasks.
 * 
 *          this service periodically checks for tasks that are in the
 *          <strong><em> PENDING </em></strong> status and updates their status
 *          to <strong><em> IN_PROGRESS </em></strong> if they have been pending
 *          for more than <em> 5 minutes </em>.
 */
@Service
@Slf4j
public class TaskStatusScheduler {

	private static final Logger logger = LoggerFactory.getLogger(TaskStatusScheduler.class);

	private TaskRepository taskRepository;

	private TaskService taskService;

	/**
	 * @Constructor for <strong> @TaskStatusScheduler </strong>
	 * 
	 * @param repository the <strong> @TaskRepository </strong> to be used for task
	 *                   operations
	 */
	public TaskStatusScheduler(TaskRepository repository, TaskService service) {
		this.taskRepository = repository;
		this.taskService = service;
	}

	/**
	 * @Scheduled method that runs every <strong> 60 seconds </strong> to update
	 *            task statuses.
	 * 
	 *            this method retrieves all tasks with a <strong> PENDING </strong>
	 *            status and checks if they have been created for more than <strong>
	 *            5 minutes </strong>. If so, their status is updated to <strong>
	 *            IN_PROGRESS </strong>.
	 */
	@Scheduled(fixedRate = 60000)
	@Transactional
	@Modifying
	public void updateTaskStatus() {
		logger.info("Checking for tasks to update status...");
		List<Task> updatePendingTasksToInProgressStatus = taskService.getTasksByStatus(Status.PENDING);
		logger.info("Found {} pending tasks. ", updatePendingTasksToInProgressStatus.size());

		for (Task task : updatePendingTasksToInProgressStatus) {
			if (task.getCreatedAt().isBefore(LocalDateTime.now(ZoneId.systemDefault()).minusMinutes(5))) {
				logger.info("Updating task ID {} from PENDING to IN_PROGRESS. ", task.getId());
				task.setStatus(Status.IN_PROGRESS);
				taskRepository.save(task);
			}
		}
		logger.info("Task status update process completed.");
	}
}