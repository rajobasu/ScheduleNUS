package me.rajobasu.shared.core.algo

import me.rajobasu.shared.addFixedTaskCommandSamples
import me.rajobasu.shared.addTaskCommandSamples
import me.rajobasu.shared.core.commands.parseAndGetCommand
import me.rajobasu.shared.core.model.Schedule
import me.rajobasu.shared.core.model.Task


class ScheduleManager {
    private val taskList = mutableListOf<Task>()
    private val sleepSchedulePreference = SleepSchedulePreference(Time.from(23, 0), Time.from(8, 0))
    private val workSchedulePreference = WorkSchedulePreference(Time.from(9, 0), Time.from(22, 0))
    lateinit var currentSchedule: Schedule

    init {
        addTaskCommandSamples.forEach {
            parseAndGetCommand(it).execute(this)
        }
        addFixedTaskCommandSamples.forEach {
            parseAndGetCommand(it).execute(this)
        }
    }

    fun addTask(task: Task): Boolean {
        currentSchedule = generateSchedule(
            taskList.apply {
                add(task)
            },
            sleepSchedulePreference,
            workSchedulePreference,
        )
        return true
    }

    fun refresh() {
        currentSchedule = generateSchedule(taskList, sleepSchedulePreference, workSchedulePreference)
    }
}