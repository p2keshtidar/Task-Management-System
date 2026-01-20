package com.example.taskmanagement.taskrepositoryintegrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.example.taskmanagement.enums.Status;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.repository.TaskRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TaskRepositoryIntegrationTest {
	
	@Autowired
	private TaskRepository taskRepository;
	
	private Task task;
	
	private Task savedTask;
	
	@BeforeEach
	public void setup() {
		Task task = new Task();
		task.setId(UUID.randomUUID());
		task.setTitle("This is a Sample Repository Test");
		task.setDescription("This is a sample repository task description");
		task.setStatus(Status.PENDING);
		task.setDueDate(LocalDate.now(ZoneId.systemDefault()).plusDays(5));
		savedTask = taskRepository.saveAndFlush(task);
	}
	
	@AfterEach
	public void clear() {
		task = null;
		savedTask = null;
	}
	
	@Test
	public void saveTask() {
		assertEquals("This is a Sample Repository Test", savedTask.getTitle());
		assertThat(savedTask).isNotNull();
	}
	
	@Test
	public void getTaskById() {
		Task tasks = taskRepository.findById(savedTask.getId()).get();
		assertEquals(tasks.getId(), savedTask.getId());
		assertThat(tasks).isNotNull();
	}
}