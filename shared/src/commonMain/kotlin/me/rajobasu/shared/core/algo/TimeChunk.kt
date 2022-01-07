package me.rajobasu.shared.core.algo

import me.rajobasu.shared.core.model.Task


class TimeChunk(
    val date: Date,
    val startTime: Time,
    val timeSpanInMins: Int,
) {
    fun getDateString(): String {
        return date.toString()
    }
}

data class TaskChunk(
    val parentTask: Task,
    val timeChunk: TimeChunk,
)

