package me.rajobasu.shared

val addTaskCommandSamples = listOf(
    "addd;120;2022-01-12 23:59;Task 1",
    "addd;120;2022-01-13 23:59;Task 2",
    "addd;120;2022-01-15 23:59;Task 3",
    "addd;120;2022-01-14 23:59;Task 4",
    "addd;150;2022-01-15 12:59;Task 5",
    "addd;200;2022-01-14 23:59;Task 6",
    "addd;200;2022-01-12 23:59;Task 7",
    "addd;80;2022-01-15 12:59;Task 8",
    "addd;100;2022-01-13 23:59;Task 9",
    "addd;200;2022-01-14 23:59;Task 10",
)

val addSingleDummyTask = listOf(
    "add;120;2022-01-12 23:59;Dummy Task"
)

val addFixedTaskCommandSamples = listOf(
    "addl;2022-01-10 12:00;120;Lecture 1",
    "addl;2022-01-09 12:00;120;Lecture 2",
    "addl;2022-01-08 12:00;120;Lecture 3",
)