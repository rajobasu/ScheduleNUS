package me.rajobasu.shared.core.commands

fun parseAndGetCommand(command: String): Command {
    if (command.startsWith("refresh", ignoreCase = true)) {
        return RefreshCommand()
    }

    val arguments = command.split(";")
    arguments.ifEmpty { throw IllegalArgumentException("Empty command") }

    if (arguments[0].trim().equals("addd", ignoreCase = true))
        return AddDeadlineCommand(arguments.drop(1))
    if (arguments[0].trim().equals("addl", ignoreCase = true))
        return AddFixedCommand(arguments.drop(1))




    throw IllegalArgumentException("No such command found")
}
