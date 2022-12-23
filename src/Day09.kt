import kotlin.math.absoluteValue
import kotlin.math.max

fun main() {

    val input = readInput("Day09")

    // part 1
    val ropeSteps = input.filter { it.isNotBlank() }.map { line ->
        RopeStep.parse(line)
    }
    ropeSteps.println()

    val headCoordinates = Coordinates(0, 0)
    val tailCoordinates = Coordinates(0, 0)

    val visitedCoordinates = mutableSetOf<Coordinates>()

    ropeSteps.forEach { step ->
        for (i in 0 until step.numberOfSteps) {
            headCoordinates.moveToDirection(step.direction)
            tailCoordinates.moveTowardsNextKnot(headCoordinates)
            visitedCoordinates.add(tailCoordinates.copy())
        }
    }
    println("part 1: ${visitedCoordinates.size}")

    // part 2
    val ropeCoordinates = mutableListOf<Coordinates>().apply {
        for (i in 0 until 10) {
            this.add(i, Coordinates(0, 0))
        }
    }.toList()
    val visitedCoordinates2 = mutableSetOf<Coordinates>()

    ropeSteps.forEach { step ->
        for (i in 0 until step.numberOfSteps) {
            ropeCoordinates[0].moveToDirection(step.direction)
            for (j in 1 .. 9) {
                ropeCoordinates[j].moveTowardsNextKnot(ropeCoordinates[j - 1])
            }
            visitedCoordinates2.add(ropeCoordinates[9].copy())
        }
    }

    println("part 2: ${visitedCoordinates2.size}")
}

private data class Coordinates(var x: Int, var y: Int) {
    fun moveToDirection(direction: RopeDirection) {
        when (direction) {
            RopeDirection.UP -> y++
            RopeDirection.RIGHT -> x++
            RopeDirection.DOWN -> y--
            RopeDirection.LEFT -> x--
        }
    }

    fun moveTowardsNextKnot(headCoordinates: Coordinates) {
        if (isDistanceTooBig(headCoordinates)) {
            if (x == headCoordinates.x) {
                if (y < headCoordinates.y) {
                    moveToDirection(RopeDirection.UP)
                } else {
                    moveToDirection(RopeDirection.DOWN)
                }
            } else if (y == headCoordinates.y) {
                if (x < headCoordinates.x) {
                    moveToDirection(RopeDirection.RIGHT)
                } else {
                    moveToDirection(RopeDirection.LEFT)
                }
            } else {
                // move diagonally
                if (x < headCoordinates.x) {
                    moveToDirection(RopeDirection.RIGHT)
                } else {
                    moveToDirection(RopeDirection.LEFT)
                }
                if (y < headCoordinates.y) {
                    moveToDirection(RopeDirection.UP)
                } else {
                    moveToDirection(RopeDirection.DOWN)
                }
            }
        }
    }

    private fun isDistanceTooBig(coordinates: Coordinates): Boolean {
        return distanceTo(coordinates) >= 2
    }

    private fun distanceTo(coordinates: Coordinates) = max(
        (x - coordinates.x).absoluteValue,
        (y - coordinates.y).absoluteValue
    )
}

private data class RopeStep(val direction: RopeDirection, val numberOfSteps: Int) {
    companion object {
        fun parse(string: String): RopeStep {
            val regex = Regex("([URDL]) (\\d+)")
            val matchResult = regex.matchEntire(string)
            matchResult?.let {
                return RopeStep(
                    RopeDirection.fromString(matchResult.groupValues[1]),
                    matchResult.groupValues[2].toInt()
                )
            }
            throw IllegalArgumentException()
        }
    }

}

private enum class RopeDirection {
    UP, RIGHT, DOWN, LEFT;

    companion object {
        fun fromString(s: String): RopeDirection {
            return when (s) {
                "U" -> UP
                "R" -> RIGHT
                "D" -> DOWN
                "L" -> LEFT
                else -> throw IllegalArgumentException()
            }
        }
    }
}