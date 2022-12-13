fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day01")
    part1(input).println()

    val elves = mutableListOf<MutableList<String>>()

    input.forEach {
        if (elves.isEmpty()) {
            elves.add(mutableListOf(it))
        } else if (it.isEmpty()) {
            elves.add(mutableListOf())
        } else {
            elves.last().add(it)
        }
    }

    elves.println()
    val elvesSortedByCalories = elves.map { l -> l.sumOf { it.toInt() } }.sortedDescending()
    // first part
    elvesSortedByCalories.first().println()
    // second part
    elvesSortedByCalories.subList(0, 3).sum().println()

}
