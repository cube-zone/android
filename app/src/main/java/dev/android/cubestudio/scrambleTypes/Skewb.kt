package dev.android.cubestudio.scrambleTypes

import kotlin.random.Random

fun scrambleSkewb(length:Int = 15): String {
    val moves = "FRBL"
    val direction: Array<String> = arrayOf("", "’")
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
        res += "$newMove${direction[Random.nextInt(2)]} "
        i++
    }
    return res
}