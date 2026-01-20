package com.example.taskmanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * this class is the main entry point for the <strong>Task Management System
 * Application</strong> to Start.
 * </p>
 * <p>
 * Enables asynchronous method execution, allowing methods annotated with to run
 * in a separate thread. Enables support for scheduled tasks, allowing methods
 * annotated with <strong> @Scheduled </strong> to be executed at fixed
 * intervals or according to cron expressions.
 * </p>
 * <p>
 * It enables asynchronous processing via <strong> @Async </strong> and supports
 * scheduled tasks with <strong> @EnableScheduling </strong>
 * </p>
 * 
 * <p>
 * this application utilizes <bold> SLF4J </bold> for <strong> @logging
 * </strong>, allowing for flexible <bold> @logging </bold> configurations and
 * support for various <strong> @logging </strong> frameworks.
 * </p>
 * <p>
 * 
 * @Logging
 *          <p>
 *          <strong>INFO:</strong> Used for general application flow and
 *          important events.
 *          </p>
 *          <p>
 *          <strong>DEBUG:</strong> Used for detailed debugging information.
 *          </p>
 *          <p>
 *          <strong>WARN:</strong> Used for potentially harmful situations.
 *          </p>
 *          <p>
 *          <strong>ERROR:</strong> Used for error events that might still allow
 *          the application to continue running.
 *          </p>
 *          <strong>SLF4J @Logger is initialized for this class to log
 *          application startup events.</strong>
 */

@SpringBootApplication
@EnableAsync
@EnableScheduling
@Slf4j
public class TaskManagementSystemApplication {

	private static final Logger logger = LoggerFactory.getLogger(TaskManagementSystemApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Task Management System Application...");
		SpringApplication.run(TaskManagementSystemApplication.class, args);
		logger.info("Task Management System Application started successfully.");
	}

}