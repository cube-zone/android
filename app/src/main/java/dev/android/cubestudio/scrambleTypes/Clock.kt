package dev.android.cubestudio.scrambleTypes

import kotlin.random.Random

fun scrambleClock(): String {
    val pins = arrayOf("R", "U", "L", "D", "RU", "LU", "RD", "LD", "ALL")
    var lastPin = ""
    var newPin = ""
    var res = ""
    var i = 0
    while(i<15) {
        if (i==9) res += "y2 "
        else {
            while (true) {
                if (lastPin == newPin) {
                    newPin = pins[Random.nextInt(pins.size)]
                } else {
                    lastPin = newPin
                    break
                }
            }
            res += newPin
            res += Random.nextInt(6)
            res += if (Random.nextBoolean()) "+ " else "- "
            i++
        }
    }
    return res
}
