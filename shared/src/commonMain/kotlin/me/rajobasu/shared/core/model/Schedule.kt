package me.rajobasu.shared.core.model

import kotlinx.datetime.LocalDateTime

class Schedule(
    val taskList: List<TaskChunk>
) {
    /**
     * This method checks if all the TaskChunk times add up to the timing of the parent Task
     */
    fun getAllUnfulfilledTasks(): List<Task> {
        return taskList.groupBy {
            it.parentTask
        }.filter {
            it.value.sumOf { x -> x.timeSpanInMins } < it.key.estimatedTimeInMinutes
        }.map {
            it.key
        }
    }

    fun minuteDeficit() {
        
    }
}

class TaskChunk(
    val parentTask: Task,
    val startTime: LocalDateTime,
    val timeSpanInMins: Int,
)