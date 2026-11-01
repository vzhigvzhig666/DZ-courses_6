package com.example.tasks

data class Task(
    val id: Long = 0,
    val title: String,
    val completed: Boolean = false
)

data class TaskRequest(
    val title: String,
    val completed: Boolean = false
)
