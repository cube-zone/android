package dev.android.cubestudio.databases.solves

data class SolveState (
    val solves: List<Solve> = emptyList(),
    val time: Long = 0,
    val newComment: String = "",
    val isEditingComment: Boolean = false
)