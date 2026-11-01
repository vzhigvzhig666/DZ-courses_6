package com.example.tasks

import java.sql.Connection
import java.sql.DriverManager

class SqliteTaskStorage(private val connection: Connection) : TaskStorage, AutoCloseable {
    init {
        connection.createStatement().use { statement ->
            statement.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    completed INTEGER NOT NULL DEFAULT 0
                )
                """.trimIndent()
            )
        }
    }

    override fun findAll(): List<Task> = connection.prepareStatement(
        "SELECT id, title, completed FROM tasks ORDER BY id"
    ).use { statement ->
        statement.executeQuery().use { result ->
            buildList {
                while (result.next()) {
                    add(
                        Task(
                            id = result.getLong("id"),
                            title = result.getString("title"),
                            completed = result.getInt("completed") != 0
                        )
                    )
                }
            }
        }
    }

    override fun findById(id: Long): Task? = connection.prepareStatement(
        "SELECT id, title, completed FROM tasks WHERE id = ?"
    ).use { statement ->
        statement.setLong(1, id)
        statement.executeQuery().use { result ->
            if (result.next()) {
                Task(
                    id = result.getLong("id"),
                    title = result.getString("title"),
                    completed = result.getInt("completed") != 0
                )
            } else {
                null
            }
        }
    }

    override fun insert(task: Task): Task {
        connection.prepareStatement(
            "INSERT INTO tasks(title, completed) VALUES (?, ?)"
        ).use { statement ->
            statement.setString(1, task.title)
            statement.setInt(2, if (task.completed) 1 else 0)
            statement.executeUpdate()
        }
        val id = connection.createStatement().use { statement ->
            statement.executeQuery("SELECT last_insert_rowid()").use { result ->
                result.next()
                result.getLong(1)
            }
        }
        return task.copy(id = id)
    }

    override fun update(task: Task): Boolean = connection.prepareStatement(
        "UPDATE tasks SET title = ?, completed = ? WHERE id = ?"
    ).use { statement ->
        statement.setString(1, task.title)
        statement.setInt(2, if (task.completed) 1 else 0)
        statement.setLong(3, task.id)
        statement.executeUpdate() > 0
    }

    override fun delete(id: Long): Boolean = connection.prepareStatement(
        "DELETE FROM tasks WHERE id = ?"
    ).use { statement ->
        statement.setLong(1, id)
        statement.executeUpdate() > 0
    }

    override fun close() {
        connection.close()
    }

    companion object {
        fun open(url: String): SqliteTaskStorage = SqliteTaskStorage(DriverManager.getConnection(url))
    }
}
