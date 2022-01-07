import me.rajobasu.shared.core.algo.ScheduleManager
import me.rajobasu.shared.core.commands.parseAndGetCommand
import me.rajobasu.shared.core.controller.Controller
import me.rajobasu.shared.core.model.Schedule

fun startIOInteraction() {
    val scheduleManager = ScheduleManager()
    val controller = Controller()

    controller.initController(scheduleManager)
    while (true) {
        val inputCommandString = readLine()

        if (inputCommandString == null || inputCommandString.startsWith("exit", ignoreCase = true)) {
            controller.exit()
            break
        }

        val command = parseAndGetCommand(inputCommandString)
        val schedule = controller.processCommand(command)

        printSchedule(schedule)
    }
}

fun printSchedule(schedule: Schedule) {
    println("=".repeat(10))
    schedule.taskList.forEach { taskChunk ->
        run {
            println("Scheduled Time: ${taskChunk.timeChunk.startTime}   \n${taskChunk.parentTask.description}")
            println("-".repeat(6))
        }
    }
    println("=".repeat(10))
}
