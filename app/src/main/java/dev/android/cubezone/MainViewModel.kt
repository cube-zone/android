package dev.android.cubezone

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.android.cubezone.databases.solves.Solve
import kotlinx.coroutines.launch

class MainViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val state: State = State()
    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    init {
        // Check if the currentSessionId is already stored in savedStateHandle
        if (savedStateHandle.contains(CURRENT_SESSION_ID_KEY)) {
            state.currentSessionId = savedStateHandle.get(CURRENT_SESSION_ID_KEY) ?: 0
        } else {
            // Initialize with the value from userPreferencesRepository
            viewModelScope.launch {
                val initialSessionId = userPreferencesRepository.fetchInitialPreferences().currentSessionId
                savedStateHandle.set(CURRENT_SESSION_ID_KEY, initialSessionId)
                state.currentSessionId = initialSessionId
            }
        }
        if (savedStateHandle.contains(CURRENT_SCRAMBLE_TYPE_KEY)) {
            state.currentScrambleType = savedStateHandle.get(CURRENT_SCRAMBLE_TYPE_KEY) ?: "3x3"
        } else {
            // Initialize with the value from userPreferencesRepository
            viewModelScope.launch {
                val initialScrambleType = userPreferencesRepository.fetchInitialPreferences().currentScrambleType
                savedStateHandle.set(CURRENT_SCRAMBLE_TYPE_KEY, initialScrambleType)
                state.currentScrambleType = initialScrambleType
            }
        }

        // Observe changes in userPreferencesFlow and update state accordingly
        viewModelScope.launch {
            userPreferencesFlow.collect { userPreferences ->
                // Update your ViewModel's state here
                state.currentScrambleType = userPreferences.currentScrambleType
                state.currentSessionId = userPreferences.currentSessionId
            }
        }
    }

    fun updateCurrentSessionId(id: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateCurrentSessionId(id)
            // Update savedStateHandle
            savedStateHandle["current_session_id"] = id
            // Update your ViewModel's state here
            state.currentSessionId = id
            Log.d("DEBUG","updateCurrentSessionId ${state.currentSessionId}")
        }
    }

    fun updateCurrentScrambleType(scrambleType: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateCurrentScrambleType(scrambleType)
            savedStateHandle["current_scramble_type"] = scrambleType
            state.currentScrambleType = scrambleType
        }
    }
    fun updateCurrentPopupSolve(solve:Solve) {
        viewModelScope.launch {
            state.currentPopupSolve = solve
        }
    }
    companion object {
        private const val CURRENT_SESSION_ID_KEY = "current_session_id"
        private const val CURRENT_SCRAMBLE_TYPE_KEY = "current_scramble_type"
    }
}

class MainViewModelFactory(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModelProvider.Factory {}
