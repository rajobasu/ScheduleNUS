package me.rajobasu.shared.core.algo

import kotlin.math.abs

const val TOTAL_MINUTES_IN_DAY = 1440
const val MINUTES_PER_HOUR = 60

class Time(
    private val minutes: Int
) {
    companion object {
        fun from(hours: Int, minutesPassed: Int) = Time(hours * 60 + minutesPassed)
    }

    fun getHours() = minutes / MINUTES_PER_HOUR
    fun getMinutes() = minutes % MINUTES_PER_HOUR
    fun getTotalMinutes() = minutes
    infix fun minus(time: Time) = Time(abs(this.minutes - time.minutes))

}

val DAY_BEGIN = Time(0)
val DAY_END = Time(TOTAL_MINUTES_IN_DAY - 1)
