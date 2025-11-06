package com.kubedroid.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kubedroid.data.ClusterConfig
import com.kubedroid.data.ClusterRepository
import com.kubedroid.kubernetes.KubernetesClientManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ClusterUiState(
    val clusters: List<ClusterConfig> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val connectedClusterId: String? = null
)

class ClusterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ClusterRepository(application)
    private val clientManager = KubernetesClientManager()

    private val _uiState = MutableStateFlow(ClusterUiState())
    val uiState: StateFlow<ClusterUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.clustersFlow.collect { clusters ->
                _uiState.value = _uiState.value.copy(clusters = clusters)
            }
        }
    }

    fun addCluster(cluster: ClusterConfig) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.addCluster(cluster)
                _uiState.value = _uiState.value.copy(isLoading = false, error = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to add cluster"
                )
            }
        }
    }

    fun removeCluster(clusterId: String) {
        viewModelScope.launch {
            try {
                if (clusterId == _uiState.value.connectedClusterId) {
                    clientManager.disconnect()
                    _uiState.value = _uiState.value.copy(connectedClusterId = null)
                }
                repository.removeCluster(clusterId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to remove cluster"
                )
            }
        }
    }

    fun connectToCluster(cluster: ClusterConfig) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            clientManager.connectToCluster(cluster)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        connectedClusterId = cluster.id,
                        error = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to connect to cluster"
                    )
                }
        }
    }

    fun getClientManager() = clientManager

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
