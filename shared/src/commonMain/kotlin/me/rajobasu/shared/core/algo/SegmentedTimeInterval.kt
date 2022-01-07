package me.rajobasu.shared.core.algo

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import me.rajobasu.shared.core.model.Task


/**
 * This class essentially acts as a single time interval, but is internally composed of several such. This can span
 * across many days, and is essentially where the scheduling will take place.
 */
interface SchedulableTimeInterval {
    fun totalMinutes(): Int

    /**
     * Key assumption here is that minutes < totalMinutes()
     */
    fun eatMinutes(minutes: Int): Pair<List<TimeChunk>, SegmentedTimeInterval>?
    fun minutesBefore(localDateTime: LocalDateTime): Int
    fun isEmpty() = totalMinutes() == 0
}


class SegmentedTimeInterval(
    private val allTimeChunks: List<TimeChunk>
) : SchedulableTimeInterval {
    override fun totalMinutes(): Int {
        return allTimeChunks.sumOf { it.timeSpanInMins }
    }

    override fun eatMinutes(minutes: Int): Pair<List<TimeChunk>, SegmentedTimeInterval> {
        val timeChunksGiven = mutableListOf<TimeChunk>()
        var totMinutesLeft = minutes
        val newTimeChunksList = allTimeChunks.toMutableList()
        while (totMinutesLeft > 0) {
            val timeChunk = newTimeChunksList.removeFirst()
            if (timeChunk.timeSpanInMins <= totMinutesLeft) {
                timeChunksGiven.add(timeChunk)
                totMinutesLeft -= timeChunk.timeSpanInMins
            } else {
                val t1 = TimeChunk(timeChunk.date, timeChunk.startTime, totMinutesLeft)
                val t2 = TimeChunk(
                    date = timeChunk.date,
                    startTime = timeChunk.startTime.add(totMinutesLeft),
                    timeSpanInMins = timeChunk.timeSpanInMins - totMinutesLeft,
                )
                timeChunksGiven.add(t1)
                newTimeChunksList.add(0, t2)
                break
            }
        }

        return Pair(timeChunksGiven, SegmentedTimeInterval(newTimeChunksList))
    }

    override fun minutesBefore(localDateTime: LocalDateTime): Int {
        TODO("Need to implement")
    }
}

fun buildTimeInterval(
    blockingTasks: List<Task>,
    sleepSchedulePreference: SleepSchedulePreference,
    workSchedulePreference: WorkSchedulePreference,
    days: Int,
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
        if (startTime != workSchedulePreference.endTime) {
            allTimeChunks.add(
                TimeChunk(
                    currentDay,
                    startTime,
                    workSchedulePreference.endTime.minus(startTime).getTotalMinutes()
                )
            )
        }

        startTime = workSchedulePreference.startTime
    }
    print(allTimeChunks)
    return SegmentedTimeInterval(allTimeChunks)
}