package dev.android.cubestudio.scrambleTypes

fun Scramble(scrambleType:String): String {
    var scramble = ""
    if (scrambleType == "Two") scramble = scrambleTwo()
    else if (scrambleType == "Three") scramble = scrambleThree()
    else if (scrambleType == "Four") scramble = scrambleFour()
    else if (scrambleType == "Five") scramble = scrambleFive()
    else if (scrambleType == "Six") scramble = scrambleSix()
    else if (scrambleType == "Seven") scramble = scrambleSeven()
    else if (scrambleType == "Pyra") scramble = scramblePyra()
    else if (scrambleType == "Square") scramble = scrambleSquare()
    else if (scrambleType == "Mega") scramble = scrambleMega()
    else if (scrambleType == "Skewb") scramble = scrambleSkewb()
    else if (scrambleType == "Clock") scramble = scrambleClock()
    return scramble
}