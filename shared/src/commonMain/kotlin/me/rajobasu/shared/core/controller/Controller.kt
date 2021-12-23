package me.rajobasu.shared.core.controller

import me.rajobasu.shared.core.algo.ScheduleManager
import me.rajobasu.shared.core.commands.Command
import me.rajobasu.shared.core.model.Schedule

class Controller {
    private lateinit var scheduleManager: ScheduleManager

    fun initController(scheduleManager: ScheduleManager) {
        this.scheduleManager = scheduleManager
    }

    fun processCommand(command: Command): Schedule {
        command.execute(scheduleManager)
        return scheduleManager.currentSchedule
    }

    fun exit() {

    }
}