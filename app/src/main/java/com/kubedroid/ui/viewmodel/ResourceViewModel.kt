package com.kubedroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kubedroid.kubernetes.KubernetesClientManager
import com.kubedroid.kubernetes.KubernetesRepository
import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.api.model.Pod
import io.fabric8.kubernetes.api.model.Service
import io.fabric8.kubernetes.api.model.apps.Deployment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResourceUiState(
    val namespaces: List<Namespace> = emptyList(),
    val pods: List<Pod> = emptyList(),
    val deployments: List<Deployment> = emptyList(),
    val services: List<Service> = emptyList(),
    val selectedNamespace: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ResourceViewModel(clientManager: KubernetesClientManager) : ViewModel() {
    private val repository = KubernetesRepository(clientManager)

    private val _uiState = MutableStateFlow(ResourceUiState())
    val uiState: StateFlow<ResourceUiState> = _uiState.asStateFlow()

    init {
        loadNamespaces()
    }

    fun loadNamespaces() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getNamespaces()
                .onSuccess { namespaces ->
                    _uiState.value = _uiState.value.copy(
                        namespaces = namespaces,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load namespaces"
                    )
                }
        }
    }

    fun loadPods(namespace: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getPods(namespace)
                .onSuccess { pods ->
                    _uiState.value = _uiState.value.copy(
                        pods = pods,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load pods"
                    )
                }
        }
    }

    fun loadDeployments(namespace: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getDeployments(namespace)
                .onSuccess { deployments ->
                    _uiState.value = _uiState.value.copy(
                        deployments = deployments,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load deployments"
                    )
                }
        }
    }

    fun loadServices(namespace: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getServices(namespace)
                .onSuccess { services ->
                    _uiState.value = _uiState.value.copy(
                        services = services,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load services"
                    )
                }
        }
    }

    fun setSelectedNamespace(namespace: String?) {
        _uiState.value = _uiState.value.copy(selectedNamespace = namespace)
    }

    fun deletePod(namespace: String, podName: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deletePod(namespace, podName)
                .onSuccess {
                    onSuccess()
                    loadPods(_uiState.value.selectedNamespace)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message ?: "Failed to delete pod"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
