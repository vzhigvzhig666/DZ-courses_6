package com.example.tasks

import java.io.IOException
import retrofit2.Response

class TaskApiClient(private val api: TaskApi) {
    fun getTasks(): List<Task> = api.getTasks().execute().requireBody()

    fun createTask(title: String): Task {
        require(title.isNotBlank()) { "Название задачи не должно быть пустым" }
        return api.createTask(TaskRequest(title.trim())).execute().requireBody()
    }

    fun deleteTask(id: Long): Boolean {
        require(id > 0) { "Id задачи должен быть положительным" }
        val response = api.deleteTask(id).execute()
        if (!response.isSuccessful) {
            throw IOException("Сервер вернул HTTP ${response.code()}")
        }
        return true
    }

    private fun <T : Any> Response<T>.requireBody(): T {
        if (!isSuccessful) {
            throw IOException("Сервер вернул HTTP ${code()}")
        }
        return body() ?: throw IOException("Сервер вернул пустой ответ")
    }
}
