import me.rajobasu.shared.core.algo.Date
import me.rajobasu.shared.core.algo.ScheduleManager

fun main() {
    println("Program starting")
    testDateAddition()
    val scheduleManager = ScheduleManager()
    println(scheduleManager.currentSchedule.getTaskChunkListForDate(0))
    println(scheduleManager.currentSchedule.getTaskChunkListForDate(1))
    val firstDate = scheduleManager.currentSchedule.getFirstDate().add(1)
    println(firstDate)
    println(firstDate.add(1) == firstDate)


    startIOInteraction()
}

fun testDateAddition() {
    val date = Date(2020, 4, 4)
    println(date)
    println(date.add(5))
    println(date.add(30))
}
