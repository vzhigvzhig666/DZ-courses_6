package com.example.tasks

import java.sql.DriverManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("CRUD для SQLite-хранилища задач")
class SqliteTaskStorageTest {
    @Test
    fun `insert find update and delete task`() {
        val storage = SqliteTaskStorage(DriverManager.getConnection("jdbc:sqlite::memory:"))
        storage.use {
            val created = it.insert(Task(title = "Написать тесты"))
            assertTrue(created.id > 0)
            assertEquals(Task(id = created.id, title = "Написать тесты"), it.findById(created.id))
            assertEquals(listOf(created), it.findAll())

            val updated = created.copy(title = "Проверить тесты", completed = true)
            assertTrue(it.update(updated))
            assertEquals(updated, it.findById(created.id))

            assertTrue(it.delete(created.id))
            assertNull(it.findById(created.id))
            assertTrue(it.findAll().isEmpty())
            assertFalse(it.delete(created.id))
        }
    }
}
