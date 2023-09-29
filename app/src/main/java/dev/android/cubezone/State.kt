package dev.android.cubezone

import dev.android.cubezone.databases.solves.Solve

data class State (
    var currentSessionId: Int = 0,
    var currentScrambleType: String = "3x3",
    var currentPopupSolve: Solve = Solve(createdAt = 0, scramble = "", sessionId = 0, time = 0)
)