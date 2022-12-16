fun main() {

    val input = readInput("Day05")

    // part 1
    val (process1, stacks1) = parseInput(input)
    process1.forEach { step ->
        stacks1.applyStep(Step.fromString(step))
    }
    stacks1.values.map { it.content.last() }.joinToString("").println()

    // part 2
    val (process2, stacks2) = parseInput(input)
    process2.forEach { step ->
        stacks2.applyStepMovingMultipleCrates(Step.fromString(step))
    }
    stacks2.values.map { it.content.last() }.joinToString("").println()
}

private fun parseInput(input: List<String>): Pair<List<String>, Map<Int, Stack>> {
    val indexOfEmptyLine = input.indexOf("")
    val situation = input.subList(0, indexOfEmptyLine)
    val process = input.subList(indexOfEmptyLine + 1, input.size)
    val situationLines = situation.map { line ->
        val chunks = line.chunked(4)
        chunks.map { it.trim().replace("[", "").replace("]", "") }
    }
    val stacks = transpose(situationLines).map { line ->
        Stack(
            number = line.last().toInt(),
            content = ArrayDeque(
                line.subList(0, line.size - 1)
                    .filter { it.isNotBlank() }
                    .map { it.toCharArray()[0] }
                    .reversed()
            )
        )
    }.associateBy { it.number }
    return Pair(process, stacks)
}

private fun Map<Int, Stack>.applyStep(step: Step) {
    for (i in 0 until  step.number) {
        val element = this[step.from]!!.content.removeLast()
        this[step.to]!!.content.addLast(element)
    }
}

private fun Map<Int, Stack>.applyStepMovingMultipleCrates(step: Step) {
    val elements = mutableListOf<Char>()
    for (i in 0 until  step.number) {
        val element = this[step.from]!!.content.removeLast()
        elements.add(element)
    }
    this[step.to]!!.content.addAll(elements.reversed())
}

private fun transpose(
    situationLines: List<List<String>>
): List<List<String>> {
    val columnIndices = situationLines.indices
    val maxRowSize = situationLines.maxOf { it.size }
    val rowIndices = 0 until maxRowSize
    return columnIndices.map { columnIndex ->
        rowIndices.map { rowIndex ->
            val element = situationLines.getOrNull(rowIndex)?.getOrNull(columnIndex)
            element ?: ""
        }
    }
}

data class Stack(val number: Int, val content: ArrayDeque<Char>)

data class Step(val number: Int, val from: Int, val to: Int) {
    companion object {
        fun fromString(string: String): Step {
            val regex = Regex("move (\\d+) from (\\d+) to (\\d+)")
            val matchResult = regex.matchEntire(string)
            return matchResult?.groupValues?.let {
                Step(it[1].toInt(), it[2].toInt(), it[3].toInt())
            } ?: throw IllegalArgumentException()
        }
    }
}
