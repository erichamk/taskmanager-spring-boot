package com.example.taskmanager;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.ExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest
@AutoConfigureRestTestClient
@Sql(scripts = "/data.sql", executionPhase = BEFORE_TEST_CLASS)
class TaskmanagerApplicationTests {

    @Autowired
    private RestTestClient client;

    @Test
    @Transactional
    void createTaskTest() {
        // Create a task
        TaskRequest task = new TaskRequest();
        task.setTitle("test");
        task.setDescription("test");
        task.setOwner("test");

        ExchangeResult response = client.post()
                .uri("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .body(task)
                .exchange()
                .expectStatus().isCreated().returnResult();

        URI location = response.getResponseHeaders().getLocation();
        assertEquals("/api/tasks/5", location.getPath());
    }

    @Test
    void getTaskTest() {

        // Get task by ID
        TaskResponse createdTask = client.get()
                .uri("/api/tasks/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .returnResult()
                .getResponseBody();

        assertEquals("Task 1", createdTask.getTitle());
        assertEquals("Task description 1", createdTask.getDescription());

    }

    @Test
    void getAllTasksTest() {

        // Get all tasks
        List<TaskResponse> tasks = client.get()
                .uri("/api/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TaskResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        assertEquals(4, tasks.size());
        assertEquals("Task 4", tasks.get(3).getTitle());
    }

    @Test
    void getAllTasksPageAndFilterTest() {

        // Get all tasks
        List<TaskResponse> tasks = client.get()
                .uri("/api/tasks?page=0&size=1&owner=user2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TaskResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        assertEquals(1, tasks.size());
        assertEquals("Task 3", tasks.get(0).getTitle());
        assertEquals("user2", tasks.get(0).getOwner());
    }

    @Test
    void getAllTasksDateFilterTest() {
        String uri = "/api/tasks?startDate=" + LocalDate.now() + "&endDate=" + LocalDate.now().plusDays(1);
        System.out.println(uri);
        // Get all tasks
        List<TaskResponse> tasks = client.get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TaskResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        assertEquals(2, tasks.size());
    }

    @Test
    @Transactional
    void updateTaskTest() {

        TaskRequest task = new TaskRequest();
        task.setTitle("Task 4");
        task.setDescription("updated");

        // Update task description
        client.put()
                .uri("/api/tasks/4")
                .contentType(MediaType.APPLICATION_JSON)
                .body(task)
                .exchange()
                .expectStatus().isNoContent();

        TaskResponse updated = client.get()
                .uri("/api/tasks/4")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .returnResult()
                .getResponseBody();

        assertEquals("updated", updated.getDescription());
    }

    @Test
//    @Transactional
    void deleteTaskTest() {
        List<TaskResponse> tasks = client.get()
                .uri("/api/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TaskResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        assertEquals(4, tasks.size());

        // Delete task
        client.delete()
                .uri("/api/tasks/1")
                .exchange();

        tasks = client.get()
                .uri("/api/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TaskResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        assertEquals(3, tasks.size());
    }

}
