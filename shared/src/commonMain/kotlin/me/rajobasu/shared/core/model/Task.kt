package me.rajobasu.shared.core.model

import kotlinx.datetime.LocalDateTime

var currentTaskCount = 1;

open class Task(
    var estimatedTimeInMinutes: Int? = null,
    var description: String? = null,
    var deadline: LocalDateTime? = null,
    var startTime: LocalDateTime? = null,
    var endTime: LocalDateTime? = null,
) {
    val uid = currentTaskCount++

    override fun toString(): String {
        return "Expected Time: ${estimatedTimeInMinutes}mins, \nDeadline: $deadline ,\nDescription: $description \n"

    }

    override fun equals(other: Any?): Boolean {
        return if (other is Task) {
            uid == other.uid
        } else false
    }
}