package com.example.ollamalite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ollamalite.data.local.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.ollamalite.domain.Result
import com.example.ollamalite.domain.use_case.GetModelsUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class SettingsUiState(
    val models: List<String> = emptyList(),
    val selectedModel: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val getModelsUseCase: GetModelsUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf(SettingsUiState())
    val uiState: State<SettingsUiState> = _uiState

    val serverUrl = userPreferencesRepository.serverUrl.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    val selectedModel = userPreferencesRepository.selectedModel.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    init {
        getModels()
    }

    private fun getModels() {
        getModelsUseCase().onEach { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        models = result.data?.models?.map { it.name } ?: emptyList(),
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
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun saveServerUrl(url: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveServerUrl(url)
        }
    }

    fun saveSelectedModel(model: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveSelectedModel(model)
        }
    }
}
