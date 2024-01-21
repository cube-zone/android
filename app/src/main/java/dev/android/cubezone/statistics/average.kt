package dev.android.cubezone.statistics

import dev.android.cubezone.components.charts.Point
import kotlin.math.min


fun aoN(n: Int, times: List<Long>): Long? {
    var sum = 0L
    if (times.size < n) {
        return null
    }
    for (i in 0 until min(n, times.size)) {
        sum += times[i]
    }
    return sum / n
}

fun ao5(times: List<Long>): Long? {
    return aoN(5, times)
}
fun ao12(times: List<Long>): Long? {
    return aoN(12, times)
}
fun ao50(times: List<Long>): Long? {
    return aoN(50, times)
}
fun ao100(times: List<Long>): Long? {
    return aoN(100, times)
}
fun bestAoN(n: Int, times: List<Long>): Long? {
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
fun aonGraph(times: List<Long>): List<Long> {
    var res = mutableListOf<Long>()
    for (i in 1..times.size) {
        res.add(aoN(i, times) ?: 0)
    }
    return res
}

fun bestSingleGraph(times: List<Long>): List<Long> {
    val rtimes = times.reversed()
    val res = mutableListOf(rtimes[0])
    for (i in 1 until rtimes.size) {
        res.add(min(res[i-1], rtimes[i]))
    }
    return res.reversed()
}

fun pointsToTimes(points: List<Point>): MutableList<Long> {
    val res = mutableListOf<Long>()
    for (point in points) {
        res.add(point.y?.toLong()?:0L)
    }
    return res
}

fun timesToPoints(times: List<Long>): MutableList<Point> {
    val res = mutableListOf<Point>()
    var i =0f
    for (time in times) {
        res.add(Point(i, time.toFloat()))
        i++
    }
    return res
}

fun allAvg(times: List<Long>): Long{
    return times.sum() / times.size
}