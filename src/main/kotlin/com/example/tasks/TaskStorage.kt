package com.example.tasks

interface TaskStorage {
    fun findAll(): List<Task>
    fun findById(id: Long): Task?
    fun insert(task: Task): Task
    fun update(task: Task): Boolean
    fun delete(id: Long): Boolean
}
