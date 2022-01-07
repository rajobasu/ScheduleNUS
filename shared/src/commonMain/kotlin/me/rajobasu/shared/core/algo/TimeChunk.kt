package me.rajobasu.shared.core.algo

import kotlinx.datetime.LocalDate
import me.rajobasu.shared.core.model.Task


class TimeChunk(
    val date: LocalDate,
    val startTime: Time,
    val timeSpanInMins: Int,
)

class TaskChunk(
    val parentTask: Task,
    val timeChunk: TimeChunk,
)