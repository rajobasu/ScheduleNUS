package me.rajobasu.shared.core.model

import kotlinx.datetime.LocalDateTime

class DeadlineTask(
    private var deadline: LocalDateTime,
    estimatedTimeInMinutes: Int,
    description: String?
): Task(estimatedTimeInMinutes, description) {
    override fun toString(): String {
        return "Expected Time: ${estimatedTimeInMinutes}mins, \nDeadline: $deadline ,\nDescription: $description \n"
    }
}