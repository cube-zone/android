package dev.android.cubestudio.scrambleTypes

import kotlin.random.Random

fun scrambleFive(length: Int = 60):String {
    val moves = arrayOf(
        "R", "L", "F", "B", "U", "D",
        "Rw", "Lw", "Fw", "Bw", "Uw", "Dw",
    )
    val direction: Array<String> = arrayOf("", "2", "’")
    var lastMove = ""
    var newMove = ""
    var res = ""
    var i = 0
    while (i < length) {
        while (true) {
            if (lastMove == newMove) {
                newMove = moves[Random.nextInt(moves.size)].toString()
            }
            else {
                lastMove = newMove
                break
            }
        }
        println(newMove)
        res += "$newMove${direction[Random.nextInt(3)]} "
        i++
    }
    return res
}
