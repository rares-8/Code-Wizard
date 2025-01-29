package com.example.codewizard.data

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.getString
import com.example.codewizard.R
import kotlinx.coroutines.flow.Flow

class OfflineProjectsRepository(private val projectDao: ProjectDao) : ProjectsRepository {
    override fun getAllProjects(): Flow<List<Project>> = projectDao.getAllProjects()

    override suspend fun deleteProject(project: Project) = projectDao.delete(project)

    override suspend fun insertProject(project: Project) = projectDao.insert(project)

    override suspend fun updateProject(project: Project) = projectDao.update(project)
}