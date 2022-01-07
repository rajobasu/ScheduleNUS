package me.rajobasu.shared.core.algo

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import me.rajobasu.shared.core.model.Schedule
import me.rajobasu.shared.core.model.Task


fun generateSchedule(
    tasks: List<Task>,
    sleepSchedulePreference: SleepSchedulePreference,
    workSchedulePreference: WorkSchedulePreference
): Schedule {
    val allFixedTasks = getFixedTaskList(tasks)
    val allMovableTasks = tasks.filter { t -> !allFixedTasks.contains(t) }
    var currentTime = Time.currentTime()
    var currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    

    while (allMovableTasks.isNotEmpty()) {
        val nextTimePeriodOnSameDay =
            nextFreeWorkPeriod(allFixedTasks, sleepSchedulePreference, workSchedulePreference)
        if (nextTimePeriodOnSameDay != null) {


        } else {
            // we go to the next day.
            currentTime = workSchedulePreference.startTime
            currentDate = currentDate.plus(1, DateTimeUnit.DAY)
        }
    }

    return Schedule(listOf())
}

private fun getFixedTaskList(tasks: List<Task>): List<Task> {

}

private infix fun List<Task>.intersectsWith(tasks: List<Task>): Boolean {

}

private fun nextFreeWorkPeriod(
    tasks: List<Task>,
    sleepSchedulePreference: SleepSchedulePreference,
    workSchedulePreference: WorkSchedulePreference
): Pair<Time, Time>? {

}