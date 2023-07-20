package dev.android.cubestudio.scrambleTypes

import kotlin.random.Random

fun scrambleTwo(length:Int = 10): String {
    val moves = "RFU"
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
        println(newMove)
        res += "$newMove${direction[Random.nextInt(3)]} "
        i++
    }
    return res
}
