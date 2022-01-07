package me.rajobasu.shared.core.algo

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import me.rajobasu.shared.core.model.Task
import kotlin.math.min
import kotlin.random.Random


/**
 * This class essentially acts as a single time interval, but is internally composed of several such. This can span
 * across many days, and is essentially where the scheduling will take place.
 */
interface SchedulableTimeInterval {
    fun totalMinutes(): Int

    /**
     * Key assumption here is that minutes < totalMinutes()
     * This method just blocks the underlying sections.
     */
    fun eatMinutes(minutes: Int): Pair<List<TimeChunk>, SegmentedTimeInterval>?
    fun minutesBefore(localDateTime: LocalDateTime): Int
    fun isEmpty() = totalMinutes() == 0

}

class SimpleTimeline(
    private val schedulableTimeInterval: SchedulableTimeInterval
) {
    data class TimelineSegment(val minutesFrom: Int, val minutesEnd: Int, val task: Task)

    private val timeline = mutableSetOf<TimelineSegment>()

    init {
        timeline.add(
            TimelineSegment(
                schedulableTimeInterval.totalMinutes(),
                schedulableTimeInterval.totalMinutes() + 10,
                Task.DUMMY
            )
        )
    }

    fun blockTime(minutesFrom: Int, minutesEnd: Int, task: Task) {
        timeline.add(TimelineSegment(minutesFrom, minutesEnd, task))
    }

    fun unBlockTime(minutesFrom: Int, minutesEnd: Int) {
        val parentSegment = timeline.find { it.minutesFrom <= minutesFrom && it.minutesEnd >= minutesEnd }!!
        timeline.remove(parentSegment)
        if (parentSegment.minutesFrom < minutesFrom) {
            timeline.add(TimelineSegment(parentSegment.minutesFrom, minutesFrom, parentSegment.task))
        }
        if (parentSegment.minutesEnd > minutesEnd) {
            timeline.add(TimelineSegment(minutesEnd, parentSegment.minutesEnd, parentSegment.task))
        }
    }

    fun findTime(length: Int, deadline: Int): Int? {
        var start = 0
        for (e in timeline) {
            val nextBlock = min(e.minutesFrom, deadline)
            if (start + length > nextBlock) {
                start = e.minutesEnd
                if (nextBlock == deadline) {
                    return null
                }
            }
            return getRandomStartTime(start, nextBlock)
        }
        return null
    }

    fun convertToActualTaskChunkList(): List<TaskChunk> {
        var current = 0
        val taskChunks = mutableListOf<TaskChunk>()
        for (e in timeline) {
            if (e.minutesFrom == schedulableTimeInterval.totalMinutes()) break
            if (current < e.minutesFrom) schedulableTimeInterval.eatMinutes(e.minutesFrom - current)
            schedulableTimeInterval.eatMinutes(e.minutesEnd - e.minutesFrom)?.first?.forEach {
                taskChunks.add(TaskChunk(e.task, it))
            }
            current = e.minutesEnd
        }
        return taskChunks
    }
}

fun getRandomStartTime(from: Int, to: Int): Int {
    return Random(Clock.System.now().epochSeconds).nextInt(to / 10 + 1 - from / 10) * 10 + from
}


class SegmentedTimeInterval(
    private var allTimeChunks: List<TimeChunk>
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
        this.allTimeChunks = newTimeChunksList
        return Pair(timeChunksGiven, SegmentedTimeInterval(newTimeChunksList))
    }

    override fun minutesBefore(localDateTime: LocalDateTime): Int {
        val date = localDateTime.date.toDate()
        val time = Time.from(localDateTime)


        return allTimeChunks.filter { tc ->
            (tc.date.getTotalDays() <= date.getTotalDays()) && (tc.startTime.getTotalMinutes() <= time.getTotalMinutes())
        }.sumOf { tc ->
            if (tc.date.getTotalDays() < date.getTotalDays()) {
                tc.timeSpanInMins
            } else {
                min(
                    tc.timeSpanInMins,
                    tc.timeSpanInMins + tc.startTime.getTotalMinutes() - time.getTotalMinutes()
                )
            }
        }
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
    if (startTime.getTotalMinutes() < workSchedulePreference.startTime.getTotalMinutes()) {
        startTime = workSchedulePreference.startTime
    }
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
                    currentDay.toDate(),
                    startTime,
                    Time.from(firstTask.startTime!!).minus(startTime).getTotalMinutes()
                )
            )

            startTime = Time.from(firstTask.startTime!!).add(firstTask.estimatedTimeInMinutes)
        }
        if (startTime != workSchedulePreference.endTime) {
            allTimeChunks.add(
                TimeChunk(
                    currentDay.toDate(),
                    startTime,
                    workSchedulePreference.endTime.minus(startTime).getTotalMinutes()
                )
            )
        }

        startTime = workSchedulePreference.startTime
    }
    return SegmentedTimeInterval(allTimeChunks)
}

