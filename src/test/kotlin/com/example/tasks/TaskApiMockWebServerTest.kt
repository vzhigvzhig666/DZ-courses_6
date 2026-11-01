package com.example.tasks

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("HTTP API задач с MockWebServer")
class TaskApiMockWebServerTest {
    private lateinit var server: MockWebServer
    private lateinit var client: TaskApiClient

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()
        client = TaskApiClient(RetrofitTaskApiFactory.create(server.url("/").toString()))
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `GET returns tasks and sends expected request`() {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("""[{"id":1,"title":"Купить хлеб","completed":false}]""")
        )

        val tasks = client.getTasks()
        val request = server.takeRequest()

        assertEquals(listOf(Task(id = 1, title = "Купить хлеб")), tasks)
        assertEquals("GET", request.method)
        assertEquals("/tasks", request.path)
    }

    @Test
    fun `POST sends task and parses created task`() {
        server.enqueue(
            MockResponse()
                .setResponseCode(201)
                .addHeader("Content-Type", "application/json")
                .setBody("""{"id":8,"title":"Купить хлеб","completed":false}""")
        )

        val task = client.createTask("Купить хлеб")
        val request = server.takeRequest()

        assertEquals(Task(id = 8, title = "Купить хлеб"), task)
        assertEquals("POST", request.method)
        assertEquals("/tasks", request.path)
        assertTrue(request.body.readUtf8().contains("\"title\":\"Купить хлеб\""))
    }

    @Test
    fun `DELETE sends task id and accepts no content response`() {
        server.enqueue(MockResponse().setResponseCode(204))

        assertTrue(client.deleteTask(8))
        val request = server.takeRequest()

        assertEquals("DELETE", request.method)
        assertEquals("/tasks/8", request.path)
    }
}
