package dev.android.cubezone.scrambleTypes

import kotlin.random.Random

fun scrambleThree(length:Int = 24): String {
    val moves = "RLFBUD"
    val direction: Array<String> = arrayOf("", "2", "’")
    var lastMove = ""
    var newMove = ""
    var res = ""
    var i = 0
    while (i < length) {
        while (true) {
            if (lastMove == newMove) {
                newMove = moves[Random.nextInt(moves.length)].toString()
            }
            else {
                lastMove = newMove
                break
            }
        }
        res += "$newMove${direction[Random.nextInt(3)]} "
        i++
    }
    return res
}