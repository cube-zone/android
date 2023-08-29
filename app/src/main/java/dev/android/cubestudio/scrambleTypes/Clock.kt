package dev.android.cubestudio.scrambleTypes

import kotlin.random.Random

fun scrambleClock(): String {
    val pins = arrayOf("UR", "DR", "DL", "UL", "U", "R", "D", "L", "ALL")
    var res = ""
    var i = 0
    for (pin in pins) {
        res += pin
        res += Random.nextInt(6)
        val condition = Random.nextBoolean() || res.last() == '0' || res.last() == '6'
        res += if (condition) "+ " else "- "
    }
    res += "y2 "
    for (pin in arrayOf("U", "R", "D", "L", "ALL")) {
        res += pin
        res += Random.nextInt(6)
        val condition = Random.nextBoolean() || res.last() == '0' || res.last() == '6'
        res += if (condition) "+ " else "- "

    }
    for (pin in arrayOf("UR ", "DR ", "DL ", "UL "))
        if (Random.nextBoolean()) {
            res += pin
        }
    return res
}
