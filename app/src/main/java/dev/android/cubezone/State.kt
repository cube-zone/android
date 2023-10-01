package dev.android.cubezone

import dev.android.cubezone.databases.solves.Solve

data class State (
    var currentSessionId: Int = 0,
    var currentScrambleType: String? = null,
    var currentScramble: String = "",
    var currentPopupSolve: Solve = Solve(createdAt = 0, scramble = "", sessionId = 0, time = 0)
)