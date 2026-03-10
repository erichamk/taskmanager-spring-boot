package com.example.taskmanager;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class TaskmanagerApplicationTests {

    @Autowired
    private RestTestClient client;

    @Test
    void createTaskAndRetrieveTest() {

        // Create a task
        TaskRequest task = new TaskRequest();
        task.setTitle("test");
        task.setDescription("test");

        TaskResponse response = client.post()
                .uri("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .body(task)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .returnResult()
                .getResponseBody();

        assertEquals(task.getTitle(), response.getTitle());
        assertEquals(task.getDescription(), response.getDescription());

        // Get all tasks
        List<TaskResponse> tasks = client.get()
                .uri("/api/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TaskResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        assertEquals(1, tasks.size());
        assertEquals(task.getTitle(), tasks.get(0).getTitle());

        // Update task description
        task.setDescription("updated");
        client.put()
                .uri("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(task)
                .exchange()
                .expectStatus().isOk();

        TaskResponse updated = client.get()
                .uri("/api/tasks/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .returnResult()
                .getResponseBody();

        assertEquals("updated", updated.getDescription());

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

        assertEquals(0, tasks.size());


    }

}
