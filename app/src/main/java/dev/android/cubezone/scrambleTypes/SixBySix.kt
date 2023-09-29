package dev.android.cubezone.scrambleTypes

import kotlin.random.Random

fun scrambleSix(length: Int = 80):String {
    val moves = arrayOf(
        "R", "L", "F", "B", "U", "D",
        "Rw", "Lw", "Fw", "Bw", "Uw", "Dw",
        "3Rw", "3Lw", "3Fw", "3Bw", "3Uw", "3Dw",
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
