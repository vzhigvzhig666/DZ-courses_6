package com.example.tasks

class TaskService(private val storage: TaskStorage) {
    fun listTasks(): List<Task> = storage.findAll()

    fun findTask(id: Long): Task? = storage.findById(id)

    fun addTask(title: String): Task {
        require(title.isNotBlank()) { "Название задачи не должно быть пустым" }
        return storage.insert(Task(title = title.trim()))
    }

    fun updateTask(task: Task): Boolean {
        require(task.id > 0) { "У задачи должен быть положительный id" }
        require(task.title.isNotBlank()) { "Название задачи не должно быть пустым" }
        return storage.update(task.copy(title = task.title.trim()))
    }

    fun deleteTask(id: Long): Boolean {
        require(id > 0) { "Id задачи должен быть положительным" }
        return storage.delete(id)
    }
}
