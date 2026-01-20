package com.example.taskmanagement.enums;

/**
 * <p>
 * enumeration representing the possible statuses of a task.
 * </p>
 * 
 * this enum defines three statuses that a task can have:
 * 
 * <p>
 * <strong> PENDING: </strong> The task has been created but is not yet in progress.
 * </p>
 * <p>
 * <strong> IN_PROGRESS: </strong> The task is currently being worked on.
 * </p>
 * <p>
 * <strong> COMPLETED: </strong> The task has been finished.
 * </p>
 * 
 * this enum can be used to track the state of tasks in a task management system
 * application.
 */
public enum Status {
	/**
	 * Task is created but not started
	 */
	PENDING,

	/**
	 * Task is currently being worked on
	 */
	IN_PROGRESS,

	/**
	 * Task has been completed
	 */
	COMPLETED
}