package dev.android.cubestudio.databases.solves

data class SolveState (
    val solves: List<Solve> = emptyList(),
    val time: Long = 0,
    val createdAt: Long = 0,
    val scramble: String = "",
    val penalisation: Int? = null,
    val dnf: Boolean = false,
    val comment: String? = null,
    val wasPb: Boolean = false,
    val isSmartCube: Boolean = false,
    val sessionId: Int = 0,
    val isRandomState: Boolean = false,
    val isEditingComment: Boolean = false
)