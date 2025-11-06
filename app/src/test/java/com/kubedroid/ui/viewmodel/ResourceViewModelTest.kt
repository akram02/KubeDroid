package com.kubedroid.ui.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ResourceViewModelTest {

    @Test
    fun resourceUiState_initialStateIsCorrect() {
        val uiState = ResourceUiState()

        assertTrue(uiState.namespaces.isEmpty())
        assertTrue(uiState.pods.isEmpty())
        assertTrue(uiState.deployments.isEmpty())
        assertTrue(uiState.services.isEmpty())
        assertNull(uiState.selectedNamespace)
        assertFalse(uiState.isLoading)
        assertNull(uiState.error)
    }

    @Test
    fun resourceUiState_canSetLoadingState() {
        val uiState = ResourceUiState(isLoading = true)
        assertTrue(uiState.isLoading)
    }

    @Test
    fun resourceUiState_canSetError() {
        val errorMessage = "Failed to load resources"
        val uiState = ResourceUiState(error = errorMessage)
        assertEquals(errorMessage, uiState.error)
    }

    @Test
    fun resourceUiState_canSetSelectedNamespace() {
        val namespace = "default"
        val uiState = ResourceUiState(selectedNamespace = namespace)
        assertEquals(namespace, uiState.selectedNamespace)
    }
}
