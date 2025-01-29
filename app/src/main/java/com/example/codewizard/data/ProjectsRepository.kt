package com.example.codewizard.data

import kotlinx.coroutines.flow.Flow

interface ProjectsRepository {
    fun getAllProjects(): Flow<List<Project>>

    suspend fun insertProject(project: Project)

    suspend fun deleteProject(project: Project)

    suspend fun updateProject(project: Project)
}