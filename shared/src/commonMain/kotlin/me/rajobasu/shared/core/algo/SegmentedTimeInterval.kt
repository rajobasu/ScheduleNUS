package me.rajobasu.shared.core.algo

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
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
class SegmentedTimeInterval(
    private val allTimeChunks: List<TimeChunk>
) : SchedulableTimeInterval {
    override fun totalMinutes(): Int {
        return allTimeChunks.sumOf { it.timeSpanInMins }
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
    days: Int,
    startTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
): SchedulableTimeInterval {
    val sortedBlockingTasks = blockingTasks.sortedBy { it.startTime }
    var startDay = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var startTime = Time.currentTime()
    val allTimeChunks = mutableListOf<TimeChunk>()

    for (i in 0 until days) {
        val currentDay = startDay.plus(i, DateTimeUnit.DAY)
        val allTasksOnCurrentDay = blockingTasks.filter { task -> task.startTime!!.date == currentDay }.toMutableList()

        while (allTasksOnCurrentDay.isNotEmpty()) {
            val firstTask = allTasksOnCurrentDay.removeFirst()
            if (startTime == Time.from(firstTask.startTime!!)) {
                continue
            }

            allTimeChunks.add(
                TimeChunk(
                    currentDay,
                    startTime,
                    Time.from(firstTask.startTime!!).minus(startTime).getTotalMinutes()
                )
            )

            startTime = Time.from(firstTask.startTime!!).add(firstTask.estimatedTimeInMinutes)
        }


        startTime = workSchedulePreference.startTime
    }
    return SegmentedTimeInterval(allTimeChunks)
}