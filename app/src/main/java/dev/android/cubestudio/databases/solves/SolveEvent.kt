package dev.android.cubestudio.databases.solves

import dev.android.cubestudio.databases.sessions.SessionEvent

sealed interface SolveEvent {
    object SaveSolve: SolveEvent
    data class SetSolve(
        val time: Long,
        val createdAt: Long,
        val scramble: String,
        val penalisation: Int? = null,
        val dnf: Boolean = false,
        val comment: String? = null,
        val wasPb: Boolean = false,
        val isSmartCube: Boolean = false,
        val sessionId: Int,
        val isRandomState: Boolean = false,
        val id: Int? = null
    ): SolveEvent
    data class PenaliseSolve(val solve: Solve, val value: Int): SolveEvent
    data class DnfSolve(val solve: Solve, val value: Boolean): SolveEvent
    data class DeleteSolve(val solve: Solve): SolveEvent
    data class EditComment(val solve: Solve, val text: String):SolveEvent
    object ShowEditCommentDialog: SolveEvent
    object HideEditCommentDialog: SolveEvent
}
