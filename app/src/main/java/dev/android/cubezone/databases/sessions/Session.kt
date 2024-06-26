package dev.android.cubezone.databases.sessions

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session (
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int? = null,
    val createdAt: Long,
    val sessionName: String,
    val scrambleType: String,
    val lastUsedAt: Int = 0
)