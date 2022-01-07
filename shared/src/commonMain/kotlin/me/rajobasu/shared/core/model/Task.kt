package me.rajobasu.shared.core.model

import kotlinx.datetime.LocalDateTime

var currentTaskCount = 1;

sealed class TaskType {
    sealed class FluidTask : TaskType() {
        object DeadlineTask : FluidTask()
        object GoalTask : FluidTask()
    }

    sealed class FixedTask : TaskType() {
        object SleepTask : FixedTask()
        object LectureTask : FixedTask()
    }
}


open class Task(
    var estimatedTimeInMinutes: Int,
    var description: String? = null,
    var deadline: LocalDateTime? = null,
    var startTime: LocalDateTime? = null,
    var endTime: LocalDateTime? = null,
    var taskType: TaskType,
) {
    val uid = currentTaskCount++

    override fun toString(): String {
        return "Expected Time: ${estimatedTimeInMinutes}mins, \nDeadline: $deadline ,\nDescription: $description \n"

    }

    override fun equals(other: Any?): Boolean {
        return if (other is Task) {
            uid == other.uid
        } else false
    }
}