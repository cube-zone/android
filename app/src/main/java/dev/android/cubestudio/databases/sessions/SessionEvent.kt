package dev.android.cubestudio.databases.sessions

sealed interface SessionEvent {
    object SaveSession: SessionEvent
    data class DeleteSession(val session: Session)
}