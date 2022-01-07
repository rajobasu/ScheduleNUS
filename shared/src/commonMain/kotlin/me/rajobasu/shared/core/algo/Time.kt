package me.rajobasu.shared.core.algo

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
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

    override fun toString(): String {
        return "${getHours()}:${getMinutes()}"
    }

    override fun hashCode(): Int {
        return minutes
    }
}

val DAY_BEGIN = Time(0)
val DAY_END = Time(TOTAL_MINUTES_IN_DAY - 1)
val monthDays = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)


class Date(
    val years: Int,
    val months: Int,
    val days: Int,
) {
    fun add(offset: Int) = LocalDate(years, months, days).plus(offset, DateTimeUnit.DAY).toDate()


    fun getTotalDays() = monthDays.subList(0, months - 1).sum() + days
    override fun equals(other: Any?): Boolean {
        return if (other is Date) this.years == other.years && this.months == other.months && this.days == this.days
        else false
    }

    override fun hashCode(): Int {
        var result = years
        result = 31 * result + months
        result = 31 * result + days
        return result
    }
}

fun LocalDate.toDate() = Date(years = this.year, months = this.monthNumber, days = this.dayOfMonth)