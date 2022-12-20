fun main() {

    val input = readInput("Day07")

    // part 1
    val baseDirectory = Directory("/")
    var currentDirectory: Directory? = null
    input.forEach { line ->
        if (isCommand(line)) {
            when (val command = Command.parseCommand(line)) {
                is Command.Change -> {
                    currentDirectory = when(command.dirName) {
                        "/" -> baseDirectory
                        ".." -> currentDirectory?.parent ?: throw IllegalArgumentException()
                        else -> {
                            currentDirectory?.let {
                                val find = it.children.filterIsInstance<Directory>().find { dir -> dir.name == command.dirName }
                                find ?: throw IllegalArgumentException()
                            } ?: throw IllegalArgumentException()
                        }
                    }
                }
                is Command.List -> {
                }
            }
        } else {
            val fileOrDirectory = FileOrDirectory.parse(line)
            fileOrDirectory.parent = currentDirectory
            currentDirectory?.children?.add(fileOrDirectory)
        }
    }

    val smallDirectories = baseDirectory.findSmallDirectories()


    println("small directories sum: ${smallDirectories.sumOf { it.size }}")

    // part 2

    val difference = 30000000 - (70000000 - baseDirectory.size)

    val candidates = baseDirectory.findDirectoriesWithAtLeastSize(difference)
    candidates.minByOrNull { it.size }?.size.println()
}

fun isCommand(line: String): Boolean = line.startsWith("$")

sealed class FileOrDirectory {
    abstract val name: String
    abstract val size: Int
    abstract var parent: Directory?

    companion object {
        fun parse(input: String): FileOrDirectory {
            val dir = Directory.parse(input)
            dir?.let { return it }
            val file = File.parse(input)
            file?.let { return it }
            throw IllegalArgumentException()
        }

    }
}

data class Directory(
    override val name: String,
    val children: MutableSet<FileOrDirectory> = mutableSetOf(),
) : FileOrDirectory() {
    fun findSmallDirectories(): Set<Directory> {
        val directoryChildren = children.filterIsInstance<Directory>()
        val directChildren = directoryChildren.filter { dir -> dir.size <= 100000 }.toSet()
        val transitiveChildren = directoryChildren.map { dir -> dir.findSmallDirectories() }.fold(setOf<Directory>()) { acc, directories ->
            acc + directories
        }
//        println("directory $name direct: $directChildren transitive $transitiveChildren")
        return directChildren + transitiveChildren
    }

    override var parent: Directory? = null
    override val size: Int
        get() = children.sumOf { it.size }

    override fun equals(other: Any?): Boolean {
        return other is Directory && other.name == name && other.parent?.name == parent?.name
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Directory[name=$name, size=$size, children=$children]"
    }

    fun findDirectoriesWithAtLeastSize(size: Int): Set<Directory> {
        val directoryChildren = children.filterIsInstance<Directory>()
        val directChildren = directoryChildren.filter { dir -> dir.size >= size }.toSet()
        val transitiveChildren = directoryChildren.map { dir -> dir.findDirectoriesWithAtLeastSize(size) }.fold(setOf<Directory>()) { acc, directories ->
            acc + directories
        }
        return directChildren + transitiveChildren
    }

    companion object {
        private val regex = Regex("dir (.+)")

        fun parse(input: String): Directory? {
            val matchResult = regex.matchEntire(input)
            return if (matchResult != null) {
                Directory(matchResult.groupValues[1])
            } else {
                null
            }
        }

    }
}

data class File(
    override val name: String,
    override val size: Int,
) : FileOrDirectory() {
    override var parent: Directory? = null
    companion object {
        private val regex = Regex("(\\d+) (.+)")

        fun parse(input: String): File? {
            val matchResult = regex.matchEntire(input)
            return if (matchResult != null) {
                File(name = matchResult.groupValues[2], size = matchResult.groupValues[1].toInt())
            } else {
                null
            }
        }

    }
}

sealed class Command {

    data class Change(val dirName: String): Command() {
        companion object {
            private val regex = Regex("\\$ cd (.+)")

            fun parse(input: String): Command? {
                val matchResult = regex.matchEntire(input)
                return if (matchResult != null) {
                    Change(matchResult.groupValues[1])
                } else {
                    null
                }
            }
        }
    }

    object List : Command() {
        fun parse(input: String): Command? {
            return if (input.trim() == "$ ls") {
                List
            } else {
                null
            }
        }
    }

    companion object {
        fun parseCommand(input: String): Command {
            val listCommand = List.parse(input)
            listCommand?.let { return it }
            val changeCommand = Change.parse(input)
            changeCommand?.let { return it }
            throw IllegalArgumentException()
        }
    }
}
