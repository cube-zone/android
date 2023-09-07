package dev.android.cubestudio.databases.sessions

data class SessionState (
    val sessions: List<Session> = emptyList(),
    val sessionName: String = "",
    val scrambleType: String = "",
    val createdAt: Long = 0,
    val lastUsedAt: Int = 0,
    val isAddingSession: Boolean = false
)