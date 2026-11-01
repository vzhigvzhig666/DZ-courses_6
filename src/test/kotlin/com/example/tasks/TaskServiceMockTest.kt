package com.example.tasks

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Сервис задач с mock-хранилищем")
class TaskServiceMockTest {
    private val storage = mockk<TaskStorage>()
    private val service = TaskService(storage)

    @Test
    fun `add task trims title and delegates to storage`() {
        val expected = Task(id = 7, title = "Купить молоко")
        every { storage.insert(Task(title = "Купить молоко")) } returns expected

        val actual = service.addTask("  Купить молоко  ")

        assertEquals(expected, actual)
        verify(exactly = 1) { storage.insert(Task(title = "Купить молоко")) }
    }

    @Test
    fun `list tasks returns storage result`() {
        val expected = listOf(Task(id = 1, title = "Первая"), Task(id = 2, title = "Вторая"))
        every { storage.findAll() } returns expected

        assertEquals(expected, service.listTasks())
    }

    @Test
    fun `find task delegates id to storage`() {
        val expected = Task(id = 5, title = "Задача")
        every { storage.findById(5) } returns expected

        assertEquals(expected, service.findTask(5))
        verify { storage.findById(5) }
    }

    @Test
    fun `update task trims title and returns storage result`() {
        every { storage.update(Task(id = 3, title = "Обновлено", completed = true)) } returns true

        val updated = service.updateTask(Task(id = 3, title = " Обновлено ", completed = true))

        assertTrue(updated)
        verify { storage.update(Task(id = 3, title = "Обновлено", completed = true)) }
    }

    @Test
    fun `delete task delegates id to storage`() {
        every { storage.delete(4) } returns false

        assertFalse(service.deleteTask(4))
        verify { storage.delete(4) }
    }

    @Test
    fun `blank task title is rejected`() {
        assertThrows(IllegalArgumentException::class.java) { service.addTask("  ") }
        verify(exactly = 0) { storage.insert(any()) }
    }
}
