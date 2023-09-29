package dev.android.cubezone.scrambleTypes

import kotlin.random.Random


fun scramblePyra(length: Int = 9): String {
    val moves = "RLBU"
    val tips = "rlbu"
    val direction: Array<String> = arrayOf("", "’")
    var lastMove = ""
    var newMove = ""
    var res = ""
    var i = 0
    while (i < length) {
        while (true) {
            if (lastMove == newMove) {
                newMove = moves[Random.nextInt(moves.length)].toString()
            } else {
                lastMove = newMove
                break
            }
        }
        res += "$newMove${direction[Random.nextInt(2)]} "
        i++
    }
    for (tip in tips) {
        if (Random.nextInt(3) > 0) {
            res += "$tip${direction[Random.nextInt(2)]} "
        }
    }
    return res
}
