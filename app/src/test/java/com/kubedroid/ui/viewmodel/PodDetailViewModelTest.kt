package com.kubedroid.ui.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class PodDetailViewModelTest {

    @Test
    fun podDetailUiState_initialStateIsCorrect() {
        val uiState = PodDetailUiState()

        assertEquals("", uiState.logs)
        assertFalse(uiState.isLoading)
        assertNull(uiState.error)
    }

    @Test
    fun podDetailUiState_canSetLoadingState() {
        val uiState = PodDetailUiState(isLoading = true)
        assertTrue(uiState.isLoading)
    }

    @Test
    fun podDetailUiState_canSetError() {
        val errorMessage = "Failed to load logs"
        val uiState = PodDetailUiState(error = errorMessage)
        assertEquals(errorMessage, uiState.error)
    }

    @Test
    fun podDetailUiState_canSetLogs() {
        val logs = "Sample log output\nLine 2\nLine 3"
        val uiState = PodDetailUiState(logs = logs)
        assertEquals(logs, uiState.logs)
    }

    @Test
    fun podDetailUiState_handlesEmptyLogs() {
        val uiState = PodDetailUiState(logs = "")
        assertEquals("", uiState.logs)
    }
}
