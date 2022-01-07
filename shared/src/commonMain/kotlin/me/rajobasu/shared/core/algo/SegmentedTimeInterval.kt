package me.rajobasu.shared.core.algo

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.rajobasu.shared.core.model.Task


interface SchedulableTimeInterval {
    fun totalMinutes(): Int
    fun eatMinutes(minutes: Int): Pair<List<TimeChunk>, SegmentedTimeInterval>?
    fun minutesBefore(localDateTime: LocalDateTime): Int
    fun isEmpty() = totalMinutes() == 0
}


/**
 * This class essentially acts as a single time interval, but is internally composed of several such. This can span
 * across many days, and is essentially where the scheduling will take place.
 */
class SegmentedTimeInterval : SchedulableTimeInterval {
    override fun totalMinutes(): Int {
        TODO("Not yet implemented")
    }

    override fun eatMinutes(minutes: Int): Pair<List<TimeChunk>, SegmentedTimeInterval> {
        TODO("Not yet implemented")
    }

    override fun minutesBefore(localDateTime: LocalDateTime): Int {
        TODO("Not yet implemented")
    }
}

fun buildTimeInterval(
    blockingTasks: List<Task>,
    sleepSchedulePreference: SleepSchedulePreference,
    workSchedulePreference: WorkSchedulePreference,
    startTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
): SchedulableTimeInterval {
    return SegmentedTimeInterval()
}