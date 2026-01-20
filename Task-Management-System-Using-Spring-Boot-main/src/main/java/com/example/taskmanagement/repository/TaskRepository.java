package com.example.taskmanagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskmanagement.enums.Status;
import com.example.taskmanagement.model.Task;

/**
 * @Repository
 *             <p>
 *             interface for managing <strong> @Task </strong> entities.
 *             </p>
 * 
 *             <p>
 *             this interface extends <strong> @JpaRepository </strong> ,
 *             providing CRUD operations and custom query methods for Task
 *             entities identified by a unique <strong> @UUID </strong> .
 *             </p>
 * 
 *             <p>
 *             The repository is responsible for interacting with the database
 *             to perform operations related to <strong> @Task </strong>
 *             entities, such as saving, deleting, and retrieving tasks based on
 *             their status.
 *             </p>
 */
public interface TaskRepository extends JpaRepository<Task, UUID> {

	/**
	 * Finds all tasks with the specified status.
	 * 
	 * @param @Status - the status of the tasks to retrieve
	 * @return a list <strong> @List </strong> <strong> @Task </strong> of tasks
	 *         that match the given status
	 */
	List<Task> findByStatus(Status status);
}