package dev.android.cubestudio.databases.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionViewModel(private val dao: SessionDao): ViewModel() {
    private val _sessions = dao.getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(SessionState())
    val state = combine(_state, _sessions) {state, sessions ->
        state.copy(
            sessions = sessions
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionState())
    fun onEvent(event: SessionEvent) {
        when (event) {
            is SessionEvent.DeleteSession -> {
                viewModelScope.launch {
                    dao.deleteSession(event.session)
                }
            }
            SessionEvent.SaveSession -> {
                val sessionName = state.value.sessionName
                if (sessionName.isBlank()) {
                    return
                }
                val session = Session(
                    sessionName = sessionName,
                    createdAt = System.currentTimeMillis(),
                    scrambleType = "3x3", //TODO make this the current session
                )
                viewModelScope.launch {
                    dao.upsertSession(session)
                }
                _state.update {
                    it.copy(
                        isAddingSession = false,
                        sessionName = ""
                    )
                }
            }

            SessionEvent.HideAddSessionDialog -> {
                _state.update {
                    it.copy(
                        isAddingSession = false
                    )
                }
            }
            SessionEvent.ShowAddSessionDialog -> {
                _state.update {
                    it.copy(
                        isAddingSession = true
                    )
                }
            }
        }
    }
}