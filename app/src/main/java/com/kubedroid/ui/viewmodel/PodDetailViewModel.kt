package com.kubedroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kubedroid.kubernetes.KubernetesClientManager
import com.kubedroid.kubernetes.KubernetesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PodDetailUiState(
    val logs: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class PodDetailViewModel(clientManager: KubernetesClientManager) : ViewModel() {
    private val repository = KubernetesRepository(clientManager)

    private val _uiState = MutableStateFlow(PodDetailUiState())
    val uiState: StateFlow<PodDetailUiState> = _uiState.asStateFlow()

    fun loadLogs(namespace: String, podName: String, containerName: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getPodLogs(namespace, podName, containerName)
                .onSuccess { logs ->
                    _uiState.value = _uiState.value.copy(
                        logs = logs,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load logs"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
