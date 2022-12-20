fun main() {

    val input = readInput("Day06")

    // part 1
    val line = input[0]
    val markerIndex = findMarker(line, 4)
    markerIndex.println()

    // part 2
    val markerIndex2 = findMarker(line, 14)
    markerIndex2.println()
}

private fun findMarker(line: String, length: Int) = line.indices.first { i ->
    return@first if (i + length >= line.length) {
        false
    } else {
        line.substring(i, i + length).hasNoDuplicates()
    }
} + length

private fun String.hasNoDuplicates(): Boolean {
    return this.chars().distinct().count() == this.chars().count()
}
