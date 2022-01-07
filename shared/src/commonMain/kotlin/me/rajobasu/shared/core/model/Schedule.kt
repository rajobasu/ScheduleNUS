package me.rajobasu.shared.core.model

import me.rajobasu.shared.core.algo.Date
import me.rajobasu.shared.core.algo.TaskChunk

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
            it.value.sumOf { x -> x.timeChunk.timeSpanInMins } < it.key.estimatedTimeInMinutes
        }.map {
            it.key
        }
    }

    fun minuteDeficit() {

    }

    fun getFirstDate(): Date {
        return taskList.first().timeChunk.date
    }

    fun getTaskChunkListForDate(date: Date) = taskList.filter { it.timeChunk.date == date }
    fun getTaskChunkListForDate(offset: Int) = getTaskChunkListForDate(getFirstDate().add(offset))
}
