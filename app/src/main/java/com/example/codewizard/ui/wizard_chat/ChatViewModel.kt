package com.example.codewizard.ui.wizard_chat

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codewizard.BuildConfig
import com.example.codewizard.data.Project
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface Message {
    data class TextFromYou(val content: String) : Message
    data class TextFromGemini(val content: String) : Message
    data object GeminiIsThinking : Message
}

data class UiState(
    val project: Project = Project(),
    val messages: List<Message> = listOf(),
    val chatHistory: List<Content> = listOf(),
)

class ChatViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState

    private val apiKey = BuildConfig.API_KEY

    private val systemInstructions =
        "Your name is Hexford, a wizard and an expert in all fields of computer science. Your task is to help users learn in a practical manner, by giving general instructions to complete a project.\n" +
                "The first message you will receive in the chat will be a project, in this format:\n" +
                "{ \"projectName\": \"...\", \"projectDifficulty\": \"...\", \"topics\": [], \"technologies\": [], \"description\": \"\", \"instructions\": \"\", }\n" +
                "You will respond to this with a variation of \"How may I help you today\", and you will remember the details of the project.\n" +
                "In the following messages, the user will ask you questions based on the project, and you will help him understand them and provide instructions.\n" +
                "Do not ever talk about something else other than computer science. Limit your responses to around 1000 characters\n".trimIndent()

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash-002",
        apiKey = apiKey,
        generationConfig = generationConfig {
            temperature = 1f
            topK = 40
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = "text/plain"
        },
        systemInstruction = content { text(systemInstructions) }
    )
    private lateinit var chat: Chat

    fun onFirstMessage() {
        val gson: Gson = GsonBuilder().create()
        val message: String = gson.toJson(uiState.value.project)
        Log.d("first message", message)

        chat = model.startChat(_uiState.value.chatHistory)

        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            messages = currentState.messages + Message.GeminiIsThinking
        )
        viewModelScope.launch {
            val response = chat.sendMessage(message).text
            if (!response.isNullOrEmpty()) {
                val updatedState = _uiState.value
                _uiState.value = updatedState.copy(
                    messages = updatedState.messages - Message.GeminiIsThinking + Message.TextFromGemini(
                        response
                    )
                )
            }
        }
    }

    fun onNewMessageFromUser(message: String) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            messages = currentState.messages + Message.TextFromYou(message) + Message.GeminiIsThinking
        )
        viewModelScope.launch {
            val response = chat.sendMessage(message).text
            if (response != null) {
                Log.d("my message", response)
            }
            if (!response.isNullOrEmpty()) {
                val updatedState = _uiState.value
                _uiState.value = updatedState.copy(
                    messages = updatedState.messages - Message.GeminiIsThinking + Message.TextFromGemini(
                        response
                    )
                )
            }
        }
    }

    fun clearUiState() {
        _uiState.value = UiState()
    }

    fun setUiState(project: Project) {
        _uiState.value = _uiState.value.copy(project = project)
    }

}