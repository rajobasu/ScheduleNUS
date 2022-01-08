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
        simpleTimeline.blockTime(timeConsumed, timeConsumed + task.estimatedTimeInMinutes, task)
        timeConsumed += task.estimatedTimeInMinutes
    }


    repeat(10) {
        iterateImprovementOnSchedule(schedulableTimeInterval, simpleTimeline)
    }


    val allDeadlineTasksAsTaskChunks = simpleTimeline.convertToActualTaskChunkList()
    val finalTaskChunksList = allDeadlineTasksAsTaskChunks + allFixedTaskAsTimeChunks
    return Schedule(finalTaskChunksList)
}

private fun getFixedTaskList(tasks: List<Task>): List<Task> {
    return tasks.filter { task -> task.taskType is TaskType.FixedTask }
}


fun iterateImprovementOnSchedule(schedulableTimeInterval: SchedulableTimeInterval, simpleTimeline: SimpleTimeline) {
    val timeslot = simpleTimeline.getRandomTimeSlot()
    val startTime = simpleTimeline.findTime(60, schedulableTimeInterval.minutesBefore(timeslot.task.deadline!!))
    startTime?.let {
        simpleTimeline.unBlockTime(timeslot.minutesFrom, timeslot.minutesEnd)
        simpleTimeline.blockTime(startTime, startTime + timeslot.task.estimatedTimeInMinutes, timeslot.task)
    }
}

