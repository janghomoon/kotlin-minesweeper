package mine.view

import mine.dto.Coordinate

object InputView {
    const val GAME_START_NUMBER_MAX_SIZE = 2

    fun getHeight(): Int {
        println("높이를 입력하세요.")
        return readln().toIntOrNull() ?: throw RuntimeException("숫자를 입력해주세요")
    }

    fun getWidth(): Int {
        println("너비를 입력하세요.")
        return readln().toIntOrNull() ?: throw RuntimeException("숫자를 입력해주세요")
    }

    fun getMineCount(): Int {
        println("지뢰는 몇 개인가요?")
        return readln().toIntOrNull() ?: throw RuntimeException("숫자를 입력해주세요")
    }

    fun gameStart(): Coordinate {
        print("open: ")
        val input: String = readlnOrNull() ?: throw RuntimeException("좌표를 입력해주세요")
        return splitGameNumber(input)
    }

    private fun splitGameNumber(input: String): Coordinate {
        val splitNumbers = input.split(",").map { e -> e.trim().toInt() }
        require(splitNumbers.size == GAME_START_NUMBER_MAX_SIZE) { "좌표는 x와 y 2가개의 값만 입력가능합니다." }
        return Coordinate(splitNumbers.first(), splitNumbers.last())
    }
}
