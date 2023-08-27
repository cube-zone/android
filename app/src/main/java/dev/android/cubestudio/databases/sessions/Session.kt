package dev.android.cubestudio.databases.sessions

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session (
    @PrimaryKey
    val sessionId: Int,
    val createdAt: Long,
    val name: String,
    val scrambleType: String,
    val userId: Int
)