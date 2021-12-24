package me.rajobasu.shared.core.algo

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus
import me.rajobasu.shared.core.model.Task

fun generateSleepTasks(
    startDay: LocalDate,
    days: Int,
    sleepSchedulePreference: SleepSchedulePreference
): List<Task> {
    val res: MutableList<Task> = mutableListOf()
    for (day in 0 until days) {
        val shiftedDay = startDay.plus(value = day, DateTimeUnit.DAY)
        for (e in sleepSchedulePreference.getSleepPeriods()) {
            res.add(
                Task(
                    startTime = shiftedDay plus e.first,
                    endTime = shiftedDay plus e.second,
                    description = "Sleep time"
                )
            )
        }
    }
    return res
}

infix fun LocalDate.plus(time: Time): LocalDateTime {
    return LocalDateTime(
        year = this.year,
        month = this.month,
        dayOfMonth = this.dayOfMonth,
        hour = time.getHours(),
        minute = time.getMinutes(),
        second = 0,
        nanosecond = 0
    )
}