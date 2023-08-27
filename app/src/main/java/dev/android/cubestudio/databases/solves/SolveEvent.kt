package dev.android.cubestudio.databases.solves

sealed interface SolveEvent {
    object SaveSolve: SolveEvent
    data class PenaliseSolve(val solve: Solve, val value: Int): SolveEvent
    data class DnfSolve(val solve: Solve): SolveEvent
    data class DeleteSolve(val solve: Solve): SolveEvent
    data class EditComment(val text: String):SolveEvent
}
