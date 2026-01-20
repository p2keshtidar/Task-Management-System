package com.example.taskmanagement.taskcontrollerintegrationtest;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.taskmanagement.TaskManagementSystemApplication;
import com.example.taskmanagement.controller.TaskController;
import com.example.taskmanagement.dto.updatetask.UpdateTaskDTO;
import com.example.taskmanagement.dto.updatetask.UpdateTaskResponse;
import com.example.taskmanagement.enums.Status;
import com.example.taskmanagement.exceptionhandler.customexception.InvalidUUIDFormatException;
import com.example.taskmanagement.exceptionhandler.customexception.TaskNotFoundException;
import com.example.taskmanagement.service.taskservice.TaskService;


@SpringBootTest(classes = TaskManagementSystemApplication.class)
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskService taskService;
	
	@InjectMocks	
	private TaskController taskController;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		Assertions.assertNotNull(mockMvc);
	}
	
	@Test
	public void testCreateANewTask() throws Exception {
		mockMvc.perform(
				post("/v1/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"Sample Task\",\"description\":\"This is a sample task description.\",\"dueDate\":\"2024-12-05\"}"))
		.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	@Test
	public void testCreateANewTaskNoTitle() throws Exception {
		mockMvc.perform(
				post("/v1/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"description\":\"This is a sample task description.\",\"dueDate\":\"2024-12-02\"}"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title").value("Title is Mandatory"));
	}
	
	@Test
	public void testCreateANewTaskNoDescription() throws Exception {
		mockMvc.perform(
				post("/v1/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"Sample Task\",\"dueDate\":\"2024-12-02\"}"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.description").value("Description is Mandatory"));
	}
	
	@Test
	public void testCreateANewTaskNoDescriptionNoTitle() throws Exception {
		mockMvc.perform(
				post("/v1/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"dueDate\":\"2024-12-02\"}"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.description").value("Description is Mandatory"))
		.andExpect(jsonPath("$.title").value("Title is Mandatory"));
	}
	
	@Test
	public void testCreateANewTaskNoDueDate() throws Exception {
		mockMvc.perform(
				post("/v1/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"Sample Task\",\"description\":\"This is a sample task description.\"}"))
		.andExpect(status().isCreated());
	}
	
	@Test
	public void testCreateANewTaskPastDueDate() throws Exception {
		mockMvc.perform(
				post("/v1/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"Sample Task\",\"description\":\"This is a sample task description.\",\"dueDate\":\"2023-12-02\"}"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.dueDate").value("Date must be in the Future"));
	}
	
	@Test
	public void testCreateANewTaskNoRequestBody() throws Exception {
		mockMvc.perform(
				post("/v1/tasks")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.response.errorMessage").value("Request body is missing or malformed. Please provide Valid Input"));
	}
	
	@Test
	public void testUpdateAnExistingTaskWithId() throws Exception {
		
		UUID existingTaskId = UUID.randomUUID();
		
		UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO();
        updateTaskDTO.setTitle("Updated Task Title");
        updateTaskDTO.setDescription("Updated Task Description");
        updateTaskDTO.setDueDate(LocalDate.now().plusDays(5));
        
        UpdateTaskResponse response = new UpdateTaskResponse(
                existingTaskId.toString(),
                updateTaskDTO.getTitle(),
                updateTaskDTO.getDescription(),
                updateTaskDTO.getDueDate().toString(),
                Status.PENDING.toString()
            );
        
        Mockito.when(taskService.updateAnExistingTask(any(String.class), any(UpdateTaskDTO.class)))
        .thenReturn(response);
		
		mockMvc.perform(
				put("/v1/tasks/" + existingTaskId.toString())
				.contentType(MediaType.APPLICATION_JSON)
		.content("{\"title\":\"Updated Task Title\",\"description\":\"Updated Task Description. Software Engineering Interview Wellness360\",\"dueDate\":\"2024-12-03\"}"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testUpdateAnExistingTaskNoExistingId() throws Exception {
		
		UUID existingTaskId = UUID.randomUUID();
		
		UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO();
        updateTaskDTO.setTitle("Updated Task Title");
        updateTaskDTO.setDescription("Updated Task Description");
        updateTaskDTO.setDueDate(LocalDate.now().plusDays(5));
        
        Mockito.when(taskService.updateAnExistingTask(any(String.class), any(UpdateTaskDTO.class)))
        .thenThrow(new TaskNotFoundException("Task Not Found with the given UUID : " + existingTaskId.toString()));
		
		mockMvc.perform(
				put("/v1/tasks/" + existingTaskId.toString())
				.contentType(MediaType.APPLICATION_JSON)
		.content("{\"title\":\"Updated Task Title\",\"description\":\"Updated Task Description. Software Engineering Interview Wellness360\",\"dueDate\":\"2024-12-03\"}"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void testUpdateAnExistingTaskPastDueDate() throws Exception {
		
		UUID existingTaskId = UUID.randomUUID();
		
		UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO();
        updateTaskDTO.setTitle("Updated Task Title");
        updateTaskDTO.setDescription("Updated Task Description");
        updateTaskDTO.setDueDate(LocalDate.now().minusDays(1));
        
        UpdateTaskResponse response = new UpdateTaskResponse(
                existingTaskId.toString(),
                updateTaskDTO.getTitle(),
                updateTaskDTO.getDescription(),
                updateTaskDTO.getDueDate().toString(),
                Status.PENDING.toString()
            );
        
        Mockito.when(taskService.updateAnExistingTask(any(String.class), any(UpdateTaskDTO.class)))
        .thenReturn(response);
		
		mockMvc.perform(
				put("/v1/tasks/" + existingTaskId.toString())
				.contentType(MediaType.APPLICATION_JSON)
		.content("{\"title\":\"Updated Task Title\",\"description\":\"Updated Task Description. Software Engineering Interview Wellness360\",\"dueDate\":\"2024-11-27\"}"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.dueDate").value("Date must be in the Future"));
	}
	
	@Test
	public void testUpdateAnExistingTaskNoRequestBody() throws Exception {
		
		UUID existingTaskId = UUID.randomUUID();
		
		mockMvc.perform(
				put("/v1/tasks/" + existingTaskId.toString())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.response.errorMessage").value("Request body is missing or malformed. Please provide Valid Input"));
	}
	
	@Test
	public void testUpdateAnExistingTaskAtLeastOneField() throws Exception {
		
		UUID existingTaskId = UUID.randomUUID();
		
		mockMvc.perform(
				put("/v1/tasks/" + existingTaskId.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.errorMessage").value("At Least one field must be provided to Update."));
	}
	
	@Test
	public void testUpdateAnExistingTaskInValidUUID() throws Exception {
		
		String existingTaskId = UUID.randomUUID().toString() + "cv";
		
		UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO();
        updateTaskDTO.setTitle("Updated Task Title");
        
        Mockito.when(taskService.updateAnExistingTask(any(String.class), any(UpdateTaskDTO.class)))
        .thenThrow(new InvalidUUIDFormatException("Invalid UUID Format:" + existingTaskId));
        
		mockMvc.perform(
				put("/v1/tasks/" + existingTaskId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"Updated Task Title\"}"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.errorMessage").value("Invalid UUID Format:" + existingTaskId));
	}

}