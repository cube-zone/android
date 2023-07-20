package dev.android.cubestudio.scrambleTypes

import kotlin.random.Random

fun scrambleMega (rowLength: Int = 10, rowNum: Int = 7): String {
    var isR = true
    var res = ""
    for (i in 1..rowNum){
        for (i in 1..rowLength) {
            res += if (isR) "R"
            else "D"
            res += if (Random.nextBoolean()) "++ "
            else "-- "
            isR = !isR
        }
        res += if (Random.nextBoolean()) "U\n"
        else "U’\n"
    }
    return res
}