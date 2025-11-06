package com.kubedroid.ui.viewmodel

import android.app.Application
import com.kubedroid.data.ClusterConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ClusterViewModelTest {

    @Test
    fun clusterUiState_initialStateIsCorrect() {
        val uiState = ClusterUiState()

        assertTrue(uiState.clusters.isEmpty())
        assertFalse(uiState.isLoading)
        assertNull(uiState.error)
        assertNull(uiState.connectedClusterId)
    }

    @Test
    fun clusterUiState_canSetLoadingState() {
        val uiState = ClusterUiState(isLoading = true)
        assertTrue(uiState.isLoading)
    }

    @Test
    fun clusterUiState_canSetError() {
        val errorMessage = "Connection failed"
        val uiState = ClusterUiState(error = errorMessage)
        assertEquals(errorMessage, uiState.error)
    }

    @Test
    fun clusterUiState_canSetConnectedCluster() {
        val clusterId = "test-cluster-id"
        val uiState = ClusterUiState(connectedClusterId = clusterId)
        assertEquals(clusterId, uiState.connectedClusterId)
    }

    @Test
    fun clusterUiState_canHandleMultipleClusters() {
        val cluster1 = ClusterConfig(
            id = "1",
            name = "Cluster 1",
            serverUrl = "https://k8s1.example.com:6443"
        )
        val cluster2 = ClusterConfig(
            id = "2",
            name = "Cluster 2",
            serverUrl = "https://k8s2.example.com:6443"
        )

        val uiState = ClusterUiState(clusters = listOf(cluster1, cluster2))
        assertEquals(2, uiState.clusters.size)
    }
}
