package com.example.codewizard.ui.project_add_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.codewizard.CodeWizardApplication
import com.example.codewizard.data.Project
import com.example.codewizard.data.ProjectsRepository

class ProjectAddViewModel(private val projectsRepository: ProjectsRepository) : ViewModel() {
    var projectUiState by mutableStateOf(ProjectUiState())
        private set

    /**
     * Insert a new [Project] in the database
     */
    suspend fun saveProject() {
        projectsRepository.insertProject(projectUiState.project)
    }

    fun isValid(project: Project): Boolean {
        with(project) {
            return projectName.isNotBlank() && projectDifficulty.isNotBlank() && topics.isNotEmpty()
        }
    }

    fun updateProjectLevel(newLevel: String) {
        projectUiState =
            ProjectUiState(
                project = projectUiState.project.copy(projectDifficulty = newLevel),
            )
    }

    fun selectNewTechnology(selectedTechnology: String) {
        val currentTechnologies = projectUiState.project.technologies
        val updatedTechnologies = if (currentTechnologies.contains(selectedTechnology)) {
            currentTechnologies - selectedTechnology
        } else {
            if (currentTechnologies.size < 5) currentTechnologies + selectedTechnology else currentTechnologies
        }

        projectUiState =
            ProjectUiState(project = projectUiState.project.copy(technologies = updatedTechnologies))
    }

    fun updateProjectName(newName: String) {
        if (newName.length > 70) {
            return
        }

        projectUiState =
            ProjectUiState(project = projectUiState.project.copy(projectName = newName))
    }

    fun updateProjectDescription(newDescription: String) {
        if (newDescription.length > 1000) {
            return
        }
        projectUiState =
            ProjectUiState(project = projectUiState.project.copy(description = newDescription))
    }

    fun updateProjectInstructions(newInstructions: String) {
        if (newInstructions.length > 2000) {
            return
        }
        projectUiState =
            ProjectUiState(project = projectUiState.project.copy(instructions = newInstructions))
    }

    fun selectNewTopic(selectedTopic: String) {
        val currentTopics = projectUiState.project.topics
        val updatedTopics = if (currentTopics.contains(selectedTopic)) {
            currentTopics - selectedTopic
        } else {
            if (currentTopics.size < 3) currentTopics + selectedTopic else currentTopics
        }

        projectUiState =
            ProjectUiState(project = projectUiState.project.copy(topics = updatedTopics))
    }

    fun clearUiState() {
        projectUiState = ProjectUiState()
    }

    fun setProjectState(project: Project) {
        projectUiState = ProjectUiState(project = project)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CodeWizardApplication)
                val repository = application.container.projectsRepository
                ProjectAddViewModel(repository)
            }
        }
    }

}

/**
 * Represents the UI state for the project screen
 */
data class ProjectUiState(val project: Project = Project())