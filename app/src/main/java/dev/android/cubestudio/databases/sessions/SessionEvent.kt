package dev.android.cubestudio.databases.sessions

sealed interface SessionEvent {
    object SaveSession: SessionEvent
    data class SetSession(
        val sessionName: String = "",
        val scrambleType: String = "3x3",
        val createdAt: Long = 0,
        val lastUsedAt: Int = 0
    ): SessionEvent
    data class DeleteSession(val session: Session): SessionEvent
    object ShowAddSessionDialog: SessionEvent
    object HideAddSessionDialog: SessionEvent
}