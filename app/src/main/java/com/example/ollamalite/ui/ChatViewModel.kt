package com.example.ollamalite.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ollamalite.domain.Result
import com.example.ollamalite.domain.use_case.GenerateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.example.ollamalite.data.local.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val generateUseCase: GenerateUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(ChatUiState())
    val uiState: State<ChatUiState> = _uiState

    fun generate(prompt: String) {
        viewModelScope.launch {
            val userMessage = ChatMessage(prompt, isUser = true)
            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + userMessage,
                isLoading = true
            )

            val selectedModel = userPreferencesRepository.selectedModel.first() ?: "llama2" // default model

            generateUseCase(prompt, selectedModel).onEach { result ->
                when (result) {
                    is Result.Success -> {
                        val modelMessage = ChatMessage(result.data?.response ?: "", isUser = false)
                        _uiState.value = _uiState.value.copy(
                            messages = _uiState.value.messages + modelMessage,
                            isLoading = false
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                    is Result.Loading -> {
                        // Already handled when the user message is added
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}
