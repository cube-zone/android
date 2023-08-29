package dev.android.cubestudio.databases.solves

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.android.cubestudio.databases.solves.Solve
import dev.android.cubestudio.databases.solves.SolveDao
import dev.android.cubestudio.databases.solves.SolveEvent
import dev.android.cubestudio.databases.solves.SolveState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SolveViewModel(private val dao: SolveDao): ViewModel() {
    private val _solves = dao.getAllSolves()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(SolveState())
    val state = combine(_state, _solves) {state, solves ->
        state.copy(
            solves = solves
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SolveState())
    fun onEvent(event: SolveEvent) {
        when (event) {
            is SolveEvent.DeleteSolve -> {
                viewModelScope.launch {
                    dao.deleteSolve(event.solve)
                }
            }
            SolveEvent.SaveSolve -> {
                val newComment = state.value.newComment
                val time = state.value.time
                if (newComment.isBlank()) {
                    return
                }
                val solve = Solve(
                    comment = newComment,
                    createdAt = System.currentTimeMillis(),
                    scramble = "",
                    sessionId = 0,
                    time = time
                )
                viewModelScope.launch {
                    dao.updateSolve(solve)
                }
                _state.update {
                    it.copy(
                        isEditingComment = false,
                        newComment = ""
                    )
                }
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
            is SolveEvent.DnfSolve -> {
                val updatedSolve = event.solve.copy(dnf = true)
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
                val updatedSolve = event.solve.copy(penalisation = event.value)
                viewModelScope.launch {
                    dao.updateSolve(updatedSolve)
                }
            }
        }
    }
}