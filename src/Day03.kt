fun main() {

    val input = readInput("Day03")

    // part 1
    val rucksacks = input.map { content ->
        val compartment1 = content.subSequence(0, (content.length / 2))
        val compartment2 = content.subSequence(content.length / 2, content.length)

        Rucksack(compartment1.toList(), compartment2.toList())
    }

    val sum = rucksacks.sumOf { rucksack ->
        val intersection = rucksack.compartment1.intersect(rucksack.compartment2.toSet())
        intersection.first().ourCode()
    }
    sum.println()

    // part 2
    val groupedRucksacks = rucksacks.withIndex().groupBy { it.index / 3 }.map { entry -> entry.value.map { it.value } }

    groupedRucksacks.map { rucksacksInGroup ->
        // find common item and get its code
        rucksacksInGroup.map {
            it.bothCompartments.toSet()
        }.reduce { acc, chars ->
            acc.intersect(chars)
        }.map {
            it.ourCode()
        }
    }.sumOf { it.first() }.println()

}

private fun Char.ourCode(): Int {
    return if (this.isUpperCase()) {
        code - 38
    } else {
        code - 96
    }
}

data class Rucksack(val compartment1: List<Char>, val compartment2: List<Char>) {
    val bothCompartments = compartment1 + compartment2
}
