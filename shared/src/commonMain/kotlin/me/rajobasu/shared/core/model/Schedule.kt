package me.rajobasu.shared.core.model

import kotlinx.datetime.LocalDateTime

class Schedule(
    val taskList: List<Pair<LocalDateTime, Task>>
)