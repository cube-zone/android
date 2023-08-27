package dev.android.cubestudio.databases.solves

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Solve (
    val time: Long, //time in ms
    val createdAt: Long, //time in ms at start
    val scramble: String,
    val penalisation: Int,
    val dnf: Boolean,
    val comment: String,
    val wasPb: Boolean,
    val isSmartCube: Boolean,
    val userId: Int,
    val sessionId: String,
    val isRandomState: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
)
