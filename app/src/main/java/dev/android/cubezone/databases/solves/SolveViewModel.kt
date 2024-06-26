package dev.android.cubezone.databases.solves

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.android.cubezone.MainViewModel
import dev.android.cubezone.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SolveViewModel(private val dao: SolveDao, mainState:State): ViewModel() {
    private val _solvesFromSession = dao.getSolvesFromSession(sessionId = mainState.currentSessionId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _solves = dao.getAllSolves()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(SolveState())
    val state = combine(_state, _solves) {state, solves ->
        state.copy(
            solves = solves
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), SolveState())
    fun onSolveEvent(event: SolveEvent) {
        when (event) {
            is SolveEvent.SetSolve -> {
                _state.update { it.copy(
                    time = event.time,
                    createdAt = event.createdAt,
                    scramble = event.scramble,
                    penalisation = event.penalisation,
                    dnf = event.dnf,
                    comment = event.comment,
                    wasPb = event.wasPb,
                    isSmartCube = event.isSmartCube,
                    sessionId = event.sessionId,
                    isRandomState = event.isRandomState,
                ) }
            }
            is SolveEvent.DeleteSolve -> {
                viewModelScope.launch {
                    dao.deleteSolve(event.solve)
                }
            }
            is SolveEvent.SaveSolve -> {
                val time = state.value.time
                val sessionId = state.value.sessionId
                val scramble = state.value.scramble
                val createdAt = state.value.createdAt
                val comment = state.value.comment
                val penalisation = state.value.penalisation
                val dnf = state.value.dnf
                val wasPb = state.value.wasPb
                val isSmartCube = state.value.isSmartCube
                val isRandomState = state.value.isRandomState

                val solve = Solve(
                    createdAt = createdAt,
                    scramble = scramble,
                    sessionId = sessionId,
                    time = time,
                    comment = comment,
                    penalisation = penalisation,
                    dnf = dnf,
                    wasPb = wasPb,
                    isSmartCube = isSmartCube,
                    isRandomState = isRandomState
                )
                viewModelScope.launch(Dispatchers.IO) {
                    dao.upsertSolve(solve)
                }
                _state.update { it.copy(
                    time = 0,
                    createdAt = 0,
                    scramble = "",
                    penalisation = null,
                    dnf = false,
                    comment = null,
                    wasPb = false,
                    isSmartCube = false,
                    sessionId = 0,
                    isRandomState = false,
                ) }
            }
            SolveEvent.HideEditCommentDialog -> {
                _state.update {
                    it.copy(
                        isEditingComment = false
                    )
                }
            }
            SolveEvent.ShowEditCommentDialog -> {
                _state.update {
                    it.copy(
                        isEditingComment = true
                    )
                }
            }
            SolveEvent.HideSolvePopup -> {
                _state.update {
                    it.copy(
                        solvePopupIsShown = false
                    )
                }
            }
            SolveEvent.ShowSolvePopup -> {
                _state.update {
                    it.copy(
                        solvePopupIsShown = true
                    )
                }
            }
            is SolveEvent.DnfSolve -> {
                val updatedSolve = event.solve.copy(dnf = event.value)
                viewModelScope.launch {
                    dao.updateSolve(updatedSolve)
                }
            }
            is SolveEvent.EditComment -> {
                val updatedSolve = state.value.solves.find { it.solveId == event.solve.solveId }
                updatedSolve?.let {
                    val updatedCommentSolve = it.copy(comment = event.text)
                    viewModelScope.launch {
                        dao.updateSolve(updatedCommentSolve)
                    }
                }
            }
            is SolveEvent.PenaliseSolve -> {
                val updatedSolve = event.solve.copy(penalisation = event.penalisation, dnf = event.dnf)
                viewModelScope.launch {
                    dao.upsertSolve(updatedSolve)
                }
            }
        }
    }
}