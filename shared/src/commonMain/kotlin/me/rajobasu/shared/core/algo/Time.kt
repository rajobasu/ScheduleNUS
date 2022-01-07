package me.rajobasu.shared.core.algo

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs

const val TOTAL_MINUTES_IN_DAY = 1440
const val MINUTES_PER_HOUR = 60

class Time(
    private val minutes: Int
) {
    companion object {
        fun from(hours: Int, minutesPassed: Int) = Time(hours * 60 + minutesPassed)
        fun currentTime(): Time {
            val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return Time.from(currentTime.hour, currentTime.minute);
        }

        fun from(localDateTime: LocalDateTime) = Time.from(localDateTime.hour, localDateTime.minute)
    }


    fun getHours() = minutes / MINUTES_PER_HOUR
    fun getMinutes() = minutes % MINUTES_PER_HOUR
    fun getTotalMinutes() = minutes
    infix fun minus(time: Time) = Time(abs(this.minutes - time.minutes))
    infix fun add(time: Time) = Time(this.minutes + time.minutes)
    infix fun add(mins: Int) = Time(this.minutes + mins)

    override fun equals(other: Any?): Boolean {
        return if (other is Time) this.minutes == other.minutes else false
    }
}

val DAY_BEGIN = Time(0)
val DAY_END = Time(TOTAL_MINUTES_IN_DAY - 1)
