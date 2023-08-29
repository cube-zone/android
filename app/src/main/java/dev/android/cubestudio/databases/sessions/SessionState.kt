package dev.android.cubestudio.databases.sessions

data class SessionState (
    val sessions: List<Session> = emptyList(),
    val sessionName: String = "",
    val isAddingSession: Boolean = false
)