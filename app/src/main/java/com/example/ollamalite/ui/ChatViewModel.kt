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
import javax.inject.Inject

data class ChatUiState(
    val response: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val generateUseCase: GenerateUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf(ChatUiState())
    val uiState: State<ChatUiState> = _uiState

    fun generate(prompt: String) {
        generateUseCase(prompt).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.value = ChatUiState(response = result.data?.response ?: "", isLoading = false)
                }
                is Result.Error -> {
                    _uiState.value = ChatUiState(error = result.message, isLoading = false)
                }
                is Result.Loading -> {
                    _uiState.value = ChatUiState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
