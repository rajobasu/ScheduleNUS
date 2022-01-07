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

    var schedulableTimeInterval = buildTimeInterval(
        allFixedTasks,
        sleepSchedulePreference,
        workSchedulePreference,
        14
    )
    val simpleTimeline = SimpleTimeline(schedulableTimeInterval)

    // for now assume that we have only deadline tasks.
    val allFixedTaskAsTimeChunks = allFixedTasks.map { task ->
        TaskChunk(
            task, TimeChunk(
                task.startTime!!.date.toDate(), Time.from(task.startTime!!), task
                    .estimatedTimeInMinutes
            )
        )
    }

    var timeConsumed = 0
    for (task in allDeadlineTasks) {
        simpleTimeline.blockTime(timeConsumed, task.estimatedTimeInMinutes, task)
    }

    repeat(tasks.size) {
        iterateImprovementOnSchedule(simpleTimeline, tasks)
    }


    val allDeadlineTasksAsTaskChunks = simpleTimeline.convertToActualTaskChunkList()
    val finalTaskChunksList = allDeadlineTasksAsTaskChunks + allFixedTaskAsTimeChunks
    return Schedule(finalTaskChunksList)
}

private fun getFixedTaskList(tasks: List<Task>): List<Task> {
    return tasks.filter { task -> task.taskType is TaskType.FixedTask }
}


fun iterateImprovementOnSchedule(simpleTimeline: SimpleTimeline, tasks: List<Task>) {

}

class TaskSegment(
    val parentTask: Task,

    ) {

}

