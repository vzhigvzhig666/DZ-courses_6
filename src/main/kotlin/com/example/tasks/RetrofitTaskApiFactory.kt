package com.example.tasks

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitTaskApiFactory {
    fun create(baseUrl: String): TaskApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TaskApi::class.java)
}
