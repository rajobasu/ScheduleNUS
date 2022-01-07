package me.rajobasu.shared.core.algo

import me.rajobasu.shared.core.model.Schedule
import me.rajobasu.shared.core.model.Task

class ScheduleManager {
    private val taskList = mutableListOf<Task>()
    private val sleepSchedulePreference = SleepSchedulePreference(Time.from(23, 0), Time.from(8, 0))
    private val workSchedulePreference = WorkSchedulePreference(Time.from(9, 0), Time.from(10, 0))

    fun addTask(task: Task): Boolean {
        currentSchedule = generateSchedule(taskList.apply {
            add(task)
        })
        return true
    }

    var currentSchedule: Schedule = Schedule(listOf())
}