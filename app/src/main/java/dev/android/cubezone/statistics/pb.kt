package dev.android.cubezone.statistics

import android.util.Log
import kotlin.math.min

fun bestSingle(times: List<Long>): Long? {
    var best = Long.MAX_VALUE
    if (times.isEmpty()) {
        return null
    }
    for (time in times) {
        Log.d("PB", time.toString())
        best = min(best, time)
    }
    return best
}

fun bestAvg(times: List<Long>, n: Int): Long? {
    var best = Long.MAX_VALUE
    if (times.size < n) {
        return null
    }
    for (i in 0 until times.size - n + 1) {
        var sum = 0L
        for (j in i until i + n) {
            sum += times[j]
        }
        best = min(best, sum / n)
    }
    return best
}