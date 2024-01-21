package dev.android.cubezone.statistics

fun totalTime(times: List<Long>): Long {
    return times.sum()
}
fun numSolves(times: List<Long>): Int {
    return times.size
}