package me.rajobasu.shared.core.algo

import me.rajobasu.shared.core.model.Schedule
import me.rajobasu.shared.core.model.Task
import me.rajobasu.shared.core.model.TaskType


fun generateSchedule(
    tasks: List<Task>,
    sleepSchedulePreference: SleepSchedulePreference,
    workSchedulePreference: WorkSchedulePreference
): Schedule {
    val allFixedTasks = getFixedTaskList(tasks)
    val allMovableTasks = tasks.filterNot { t -> allFixedTasks.contains(t) }
    val allDeadlineTasks =
        allMovableTasks.filter { task -> task.taskType is TaskType.FluidTask.DeadlineTask }.sortedBy { it.deadline }

    var schedulableTimeInterval = buildTimeInterval(allFixedTasks, sleepSchedulePreference, workSchedulePreference)

    // for now assume that we have only deadline tasks.
    val allFixedTaskAsTimeChunks = allFixedTasks.map { task ->
        TaskChunk(task, TimeChunk(task.startTime!!.date, Time.from(task.startTime!!), task.estimatedTimeInMinutes))
    }
    val allDeadlineTasksAsTaskChunks = mutableListOf<TaskChunk>()


    for (task in allDeadlineTasks) {
        if (schedulableTimeInterval.isEmpty()) {
            break
        }
        val result = schedulableTimeInterval.eatMinutes(task.estimatedTimeInMinutes)
        result?.run {
            val timeChunks = result.first
            schedulableTimeInterval = result.second

            allDeadlineTasksAsTaskChunks.addAll(timeChunks.map { timeChunk ->
                TaskChunk(task, timeChunk)
            })
        }
    }

    val finalTaskChunksList = allDeadlineTasksAsTaskChunks + allFixedTaskAsTimeChunks
    return Schedule(finalTaskChunksList)
}

private fun getFixedTaskList(tasks: List<Task>): List<Task> {
    return tasks.filter { task -> task.taskType is TaskType.FixedTask }
}
