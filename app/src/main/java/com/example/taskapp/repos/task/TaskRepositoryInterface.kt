package com.example.taskapp.repos.task

import com.example.taskapp.database.entities.Task
import com.example.taskapp.database.entities.TaskMinimal
import org.threeten.bp.LocalDate

interface TaskRepositoryInterface {
    suspend fun getTasks(): List<Task>

    suspend fun deleteByID(id: Long): Int

    suspend fun saveTask(task: Task)

    suspend fun getMinTasksByUpdateDate(date: LocalDate = LocalDate.now()): List<TaskMinimal>

    suspend fun getTasksByUpdateDate(date: LocalDate = LocalDate.now()): List<Task>

    suspend fun getTaskByID(id: Long): Task

    suspend fun getMinimalTasks(): List<TaskMinimal>

    suspend fun updateTask(task: Task): Int
}