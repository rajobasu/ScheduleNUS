package me.rajobasu.shared.core.algo

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.rajobasu.shared.core.model.Schedule
import me.rajobasu.shared.core.model.Task


fun generateSchedule(tasks: List<Task>): Schedule {
    return Schedule(tasks.map { x -> Pair(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()), x) })
}