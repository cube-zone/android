package dev.android.cubezone.statistics

fun deviance(times: List<Long>): Long? {
    val average = aoN(times.size, times) ?: return null
    var sum = 0L
    for (time in times) {
        sum += (time - average) * (time - average)
    }
    return sum / times.size
}