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

    val schedulableTimeInterval = buildTimeInterval(allFixedTasks, sleepSchedulePreference, workSchedulePreference)


    return Schedule(listOf())
}

private fun getFixedTaskList(tasks: List<Task>): List<Task> {
    return tasks.filter { task -> task.taskType is TaskType.FixedTask }
}

private infix fun List<Task>.intersectsWith(tasks: List<Task>): Boolean {
    return this.map { task -> task.uid }.intersect(tasks.map { task -> task.uid }.toSet()).isNotEmpty()
}
