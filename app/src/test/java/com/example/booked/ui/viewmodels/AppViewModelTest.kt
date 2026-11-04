package com.example.booked.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun setup() { Dispatchers.setMain(dispatcher) }
    @After fun teardown() { Dispatchers.resetMain() }

    @Test fun splashLastsTwoSecondsThenSignInNavigatesHome() = runTest(dispatcher) {
        val saved = SavedStateHandle()
        val vm = AppViewModel(saved)
        runCurrent()
        advanceTimeBy(1_999)
        assertTrue(vm.uiState.value.showSplash)
        advanceTimeBy(1)
        runCurrent()
        assertFalse(vm.uiState.value.showSplash)
        assertFalse(vm.uiState.value.showHome)
        vm.onSignInClick()
        assertTrue(vm.uiState.value.showHome)
        val restored = AppViewModel(SavedStateHandle(saved.keys().associateWith { saved.get<Any>(it) }))
        assertEquals(vm.uiState.value, restored.uiState.value)
        assertFalse(restored.uiState.value.showSplash)
    }
}
