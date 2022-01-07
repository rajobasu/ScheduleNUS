package me.rajobasu.shared.core.algo

import kotlinx.datetime.LocalDateTime
import me.rajobasu.shared.core.model.Task


interface SchedulableTimeInterval {
    fun totalMinutes(): Int
    fun eatMinutes(minutes: Int): Pair<Boolean, SegmentedTimeInterval>
    fun minutesBefore(localDateTime: LocalDateTime): Int
}


/**
 * This class essentially acts as a single time interval, but is internally composed of several such. This can span
 * across many days, and is essentially where the scheduling will take place.
 */
class SegmentedTimeInterval : SchedulableTimeInterval {
    override fun totalMinutes(): Int {
        TODO("Not yet implemented")
    }

    override fun eatMinutes(minutes: Int): Pair<Boolean, SegmentedTimeInterval> {
        TODO("Not yet implemented")
    }

    override fun minutesBefore(localDateTime: LocalDateTime): Int {
        TODO("Not yet implemented")
    }
}

fun buildTimeInterval(
    blockingTasks: List<Task>,
    sleepSchedulePreference: SleepSchedulePreference,
    workSchedulePreference: WorkSchedulePreference
): SchedulableTimeInterval {
    return SegmentedTimeInterval()
}