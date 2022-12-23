fun main() {

    val input = readInput("Day08")

    // part 1
    val grid = Grid()
    input.forEachIndexed { y, line ->
        line.chunked(1).mapIndexed { x, value ->
            grid.addValue(x, y, value.toInt())
        }
    }

    grid.numberOfTreesVisible().println()

    // part 2
    grid.highestScenicScore().println()
}

private data class Grid(val coordinates: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()) {

    fun addValue(x: Int, y: Int, value: Int) {
        coordinates[Pair(x, y)] = value
    }

    fun numberOfTreesVisible(): Int {
        return coordinates.filter { isVisible(it.key.first, it.key.second) }.size
    }

    private fun isVisible(x: Int, y: Int): Boolean {
        return isVisibleFromDirection(Direction.TOP, x, y) ||
                isVisibleFromDirection(Direction.RIGHT, x, y) ||
                isVisibleFromDirection(Direction.BOTTOM, x,y) ||
                isVisibleFromDirection(Direction.LEFT, x, y)
    }

    private fun isVisibleFromDirection(direction: Direction, x: Int, y: Int): Boolean {
        val treesToCheck = treesInDirection(direction, x, y)
        val targetValue = coordinates[Pair(x, y)]
        targetValue?.let {
            return !treesToCheck.any { coordinate -> coordinate.second >= targetValue }
        } ?: throw IllegalStateException()
    }

    private fun treesInDirection(direction: Direction, x: Int, y: Int): List<Pair<Pair<Int, Int>, Int>> {
        val treesToCheck = when (direction) {
            Direction.TOP -> coordinates.filter { entry -> entry.key.first == x && entry.key.second < y }.toList().reversed()
            Direction.RIGHT -> coordinates.filter { entry -> entry.key.second == y && entry.key.first > x }.toList()
            Direction.BOTTOM -> coordinates.filter { entry -> entry.key.first == x && entry.key.second > y }.toList()
            Direction.LEFT -> coordinates.filter { entry -> entry.key.second == y && entry.key.first < x }.toList().reversed()
        }
        return treesToCheck
    }

    fun highestScenicScore(): Int {
        return coordinates.map { calculateScenicScore(it.key.first, it.key.second) }.maxOf { it }
    }

    fun calculateScenicScore(x: Int, y: Int): Int {
        return calculateScenicScore(x, y, Direction.TOP) *
                calculateScenicScore(x, y, Direction.RIGHT) *
                calculateScenicScore(x, y, Direction.BOTTOM) *
                calculateScenicScore(x, y, Direction.LEFT)
    }

    private fun calculateScenicScore(x: Int, y: Int, direction: Direction): Int {
        val treesInDirection = treesInDirection(direction, x, y)
        if (treesInDirection.isEmpty())
            return 0
        val targetValue = coordinates[Pair(x, y)]
        targetValue?.let {
            val indexOfFirst = treesInDirection.indexOfFirst { it.second >= targetValue }
            return if (indexOfFirst == -1) treesInDirection.size else indexOfFirst + 1
        }
        return 0
    }
}

private enum class Direction {
    TOP, RIGHT, BOTTOM, LEFT;
}
