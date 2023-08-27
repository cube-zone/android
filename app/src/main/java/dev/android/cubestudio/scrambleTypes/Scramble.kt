package dev.android.cubestudio.scrambleTypes

fun Scramble(scrambleType:String): String {
    var scramble = ""
    if (scrambleType == "2x2") scramble = scrambleTwo()
    else if (scrambleType == "3x3") scramble = scrambleThree()
    else if (scrambleType == "4x4") scramble = scrambleFour()
    else if (scrambleType == "5x5") scramble = scrambleFive()
    else if (scrambleType == "6x6") scramble = scrambleSix()
    else if (scrambleType == "7x7") scramble = scrambleSeven()
    else if (scrambleType == "Pyraminx") scramble = scramblePyra()
    else if (scrambleType == "Sq-1") scramble = scrambleSquare()
    else if (scrambleType == "Megaminx") scramble = scrambleMega()
    else if (scrambleType == "Skewb") scramble = scrambleSkewb()
    else if (scrambleType == "Clock") scramble = scrambleClock()
    return scramble
}