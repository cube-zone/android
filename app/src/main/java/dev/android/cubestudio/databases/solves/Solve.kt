package dev.android.cubestudio.databases.solves

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Solve (
    val time: Long,
    val createdAt: Long,
    val scramble: String,
    val penalisation: Int? = null,
    val dnf: Boolean = false,
    val comment: String? = null,
    val wasPb: Boolean = false,
    val isSmartCube: Boolean = false,
    val sessionId: Int,
    val isRandomState: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val solveId: Int? = null,
)
