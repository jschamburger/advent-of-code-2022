import java.lang.Integer.min

fun main() {

    val input = readInput("Day04")

    // part 1
    input.map { line ->
        line.split(",").map { assignment ->
            val split = assignment.split("-")
            Assignment(split[0].toInt(), split[1].toInt())
        }
    }.filter { assignments ->
        val first = assignments[0]
        val second = assignments[1]
        return@filter first.isContainedInOrContains(second)
    }.size.println()

    // part 2

    input.map { line ->
        line.split(",").map { assignment ->
            val split = assignment.split("-")
            Assignment(split[0].toInt(), split[1].toInt())
        }
    }.filter { assignments ->
        val first = assignments[0]
        val second = assignments[1]
        return@filter first.overlapsWith(second)
    }.size.println()


}

data class Assignment(val start: Int, val end: Int) {
    private fun values(): Set<Int> {
        return (start..end).toSet()
    }

    fun overlapsWith(other: Assignment): Boolean {
        return this.values().intersect(other.values()).isNotEmpty()
    }

    fun isContainedInOrContains(other: Assignment): Boolean {
        return this.values().intersect(other.values()).size == min(this.values().size, other.values().size)
    }
}