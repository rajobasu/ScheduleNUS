package me.rajobasu.shared.core.commands

fun parseAndGetCommand(command: String) : Command {
    val arguments = command.split(";")
    arguments.ifEmpty { throw IllegalArgumentException("Empty command") }

    if (arguments[0].trim().equals("add", ignoreCase = true))
        return AddCommand(arguments.drop(1))

    throw IllegalArgumentException("No such command found")
}