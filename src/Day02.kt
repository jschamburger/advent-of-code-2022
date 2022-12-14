fun main() {

    val input = readInput("Day02")

    val sum = input.sumOf {
        if (it.isNotBlank()) {
            val split = it.split(" ")
            val opponent = split[0]
            val player = split[1]
            calculateScore(opponent, player)
        } else 0
    }
    sum.println()

}

fun calculateScore(opponent: String, player: String): Int {
    val opponentShape = Shape.fromString(opponent)
    return when (Shape.fromString(player)) {
        Shape.ROCK -> {
            1 + when (opponentShape) {
                Shape.ROCK -> 3
                Shape.PAPER -> 0
                Shape.SCISSORS -> 6
            }
        }

        Shape.PAPER -> {
            2 + when (opponentShape) {
                Shape.ROCK -> 6
                Shape.PAPER -> 3
                Shape.SCISSORS -> 0
            }
        }

        Shape.SCISSORS -> {
            3 + when (opponentShape) {
                Shape.ROCK -> 0
                Shape.PAPER -> 6
                Shape.SCISSORS -> 3
            }
        }
    }
}

enum class Shape(private val opponentStringValue: String, private val playerStringValue: String) {
    ROCK("A", "X"), PAPER("B", "Y"), SCISSORS("C", "Z");

    companion object {
        fun fromString(string: String): Shape {
            for (shape in Shape.values()) {
                if (shape.opponentStringValue == string || shape.playerStringValue == string)
                    return shape
            }
            return ROCK
        }
    }
}
