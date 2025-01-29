package com.example.codewizard.ui.project_view_screen

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
import com.example.codewizard.ui.project_add_screen.ProjectUiState

class ProjectViewModel(private val projectsRepository: ProjectsRepository) : ViewModel() {
    var projectUiState by mutableStateOf(ProjectUiState())
        private set

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
                ProjectViewModel(repository)
            }
        }
    }
}