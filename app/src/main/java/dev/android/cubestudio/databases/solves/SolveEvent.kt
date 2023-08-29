package dev.android.cubestudio.databases.solves

import dev.android.cubestudio.databases.sessions.SessionEvent

sealed interface SolveEvent {
    object SaveSolve: SolveEvent
    data class PenaliseSolve(val solve: Solve, val value: Int): SolveEvent
    data class DnfSolve(val solve: Solve): SolveEvent
    data class DeleteSolve(val solve: Solve): SolveEvent
    data class EditComment(val solve: Solve, val text: String):SolveEvent
    object ShowEditCommentDialog: SolveEvent
    object HideEditCommentDialog: SolveEvent
}
