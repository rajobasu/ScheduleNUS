package me.rajobasu.shared.core.algo

class SleepSchedulePreference(
    private val startTime: Time,
    private val endTime: Time
) {
    fun getSleepPeriods(): List<Pair<Time, Time>> {
        return if (startTime.minutes > endTime.minutes) {
            listOf(
                Pair(startTime, DAY_END), Pair(
                    DAY_BEGIN, endTime
                )
            )
        } else listOf(Pair(startTime, endTime))
    }
}

