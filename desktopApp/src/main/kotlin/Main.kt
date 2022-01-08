import me.rajobasu.shared.core.algo.Date

fun main() {
    println("Program starting")
    startIOInteraction()
}

fun testDateAddition() {
    val date = Date(2020, 4, 4)
    println(date)
    println(date.add(5))
    println(date.add(30))
}
