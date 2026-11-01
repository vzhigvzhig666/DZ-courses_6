package com.example.tasks

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApi {
    @GET("tasks")
    fun getTasks(): Call<List<Task>>

    @POST("tasks")
    fun createTask(@Body request: TaskRequest): Call<Task>

    @DELETE("tasks/{id}")
    fun deleteTask(@Path("id") id: Long): Call<Void>
}
