package com.example.codewizard.ui.project_request

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.codewizard.BuildConfig
import com.example.codewizard.CodeWizardApplication
import com.example.codewizard.data.Project
import com.example.codewizard.data.ProjectRequest
import com.example.codewizard.data.ProjectsRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class ProjectRequestViewModel(private val projectsRepository: ProjectsRepository) : ViewModel() {
    var projectRequestUiState by mutableStateOf(ProjectRequestUiState())
        private set

    fun isValid(projectRequest: ProjectRequest): Boolean {
        with(projectRequest) {
            return projectDifficulty.isNotBlank() && topics.isNotEmpty()
        }
    }

    suspend fun generateProject(prompt: String): Project? {
        val gson: Gson = GsonBuilder().create()
        val stringRequest: String = gson.toJson(projectRequestUiState.project)

        Log.d("Project request as string: ", stringRequest)
        val apiKey = BuildConfig.API_KEY

        val model = GenerativeModel(
            modelName = "gemini-1.5-flash-002",
            apiKey = apiKey,
            generationConfig = generationConfig {
                temperature = 1f
                topK = 40
                topP = 0.95f
                maxOutputTokens = 8192
                responseMimeType = "application/json"
            },
            systemInstruction = content { text(prompt) }
        )
        val chatHistory:List<Content> = listOf()
        val chat = model.startChat(chatHistory)
        val response = chat.sendMessage(stringRequest)
        response.text?.let { Log.d("Project response as string: ", it) }

        try {
            val generatedProject: Project = gson.fromJson(response.text, Project::class.java)
            return generatedProject
        } catch (e: Exception) {
            Log.e("JSON Parsing Error", e.message ?: "Unknown error")
        }
        return null
    }

    fun updateProjectLevel(newLevel: String) {
        projectRequestUiState =
            ProjectRequestUiState(
                project = projectRequestUiState.project.copy(projectDifficulty = newLevel),
            )
    }

    fun selectNewTechnology(selectedTechnology: String) {
        val currentTechnologies = projectRequestUiState.project.technologies
        val updatedTechnologies = if (currentTechnologies.contains(selectedTechnology)) {
            currentTechnologies - selectedTechnology
        } else {
            if (currentTechnologies.size < 5) currentTechnologies + selectedTechnology else currentTechnologies
        }

        projectRequestUiState =
            ProjectRequestUiState(project = projectRequestUiState.project.copy(technologies = updatedTechnologies))
    }

    fun selectNewTopic(selectedTopic: String) {
        val currentTopics = projectRequestUiState.project.topics
        val updatedTopics = if (currentTopics.contains(selectedTopic)) {
            currentTopics - selectedTopic
        } else {
            if (currentTopics.size < 3) currentTopics + selectedTopic else currentTopics
        }

        projectRequestUiState =
            ProjectRequestUiState(project = projectRequestUiState.project.copy(topics = updatedTopics))
    }

    fun updateProjectInformation(newInformation: String) {
        if (newInformation.length > 1000) {
            return
        }

        projectRequestUiState =
            ProjectRequestUiState(project = projectRequestUiState.project.copy(additionalInformation = newInformation))
    }

    fun clearUiState() {
        projectRequestUiState = ProjectRequestUiState()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CodeWizardApplication)
                val repository = application.container.projectsRepository
                ProjectRequestViewModel(repository)
            }
        }
    }
}

/**
 * Represents the UI state for the project screen
 */
data class ProjectRequestUiState(val project: ProjectRequest = ProjectRequest())