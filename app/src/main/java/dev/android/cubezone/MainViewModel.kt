package dev.android.cubezone

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.android.cubezone.databases.solves.Solve
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
class MainViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow
    val state = MutableStateFlow(State()).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), State())
    init {
        if (savedStateHandle.contains(CURRENT_SESSION_ID_KEY)) {
            state.value.currentSessionId = savedStateHandle.get(CURRENT_SESSION_ID_KEY) ?: 0
        } else {
            viewModelScope.launch {
                val initialSessionId = userPreferencesRepository.fetchInitialPreferences().currentSessionId
                savedStateHandle.set(CURRENT_SESSION_ID_KEY, initialSessionId)
                state.value.currentSessionId = initialSessionId
            }
        }
        if (savedStateHandle.contains(CURRENT_SCRAMBLE_TYPE_KEY)) {
            state.value.currentScrambleType = savedStateHandle.get(CURRENT_SCRAMBLE_TYPE_KEY) ?: "3x3"
        } else {
            viewModelScope.launch {
                val initialScrambleType = userPreferencesRepository.fetchInitialPreferences().currentScrambleType
                savedStateHandle.set(CURRENT_SCRAMBLE_TYPE_KEY, initialScrambleType)
                state.value.currentScrambleType = initialScrambleType
            }
        }
        viewModelScope.launch {
            userPreferencesFlow.collect { userPreferences ->
                state.value.currentScrambleType = userPreferences.currentScrambleType
                state.value.currentSessionId = userPreferences.currentSessionId
            }
        }
    }

    fun updateCurrentSessionId(id: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateCurrentSessionId(id)
            savedStateHandle["current_session_id"] = id
            state.value.currentSessionId = id
            Log.d("DEBUG", "vm: ${state.value.currentSessionId}")
        }
    }

    fun updateCurrentScrambleType(scrambleType: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateCurrentScrambleType(scrambleType)
            savedStateHandle["current_scramble_type"] = scrambleType
            state.value.currentScrambleType = scrambleType
        }
    }
    fun updateCurrentScramble(scramble: String) {
        viewModelScope.launch {
            state.value.currentScramble = scramble
        }
    }
    fun updateCurrentPopupSolve(solve:Solve) {
        viewModelScope.launch {
            state.value.currentPopupSolve = solve
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
