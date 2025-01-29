package com.example.codewizard.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.codewizard.CodeWizardApplication
import com.example.codewizard.data.Project
import com.example.codewizard.data.ProjectsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val projectsRepository: ProjectsRepository): ViewModel() {
    val homeUiState: StateFlow<HomeUiState> =
        projectsRepository.getAllProjects()
            .map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    suspend fun deleteProject(project: Project) {
        projectsRepository.deleteProject(project)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CodeWizardApplication)
                val repository = application.container.projectsRepository
                HomeViewModel(repository)
            }
        }
    }
}

/**
 * Represents the UI state for the home screen
 */
data class HomeUiState(val projectList: List<Project> = listOf())