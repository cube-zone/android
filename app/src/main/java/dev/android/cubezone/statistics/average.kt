package dev.android.cubezone.statistics

import java.lang.Integer.min

fun aoN(n: Int, times: List<Long>): Long {
    var sum = 0L
    for (i in 0 until min(n, times.size)) {
        sum += times[i]
    }
    return sum / n
}

fun ao5(times: List<Long>): Long {
    return aoN(5, times)
}
fun ao12(times: List<Long>): Long {
    return aoN(12, times)
}
fun ao50(times: List<Long>): Long {
    return aoN(50, times)
}
fun ao100(times: List<Long>): Long {
    return aoN(100, times)
}
