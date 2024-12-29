package mine.domain

import mine.enums.MineCell

class MineRandomPlacer {
    fun placeMines(
        height: Int,
        width: Int,
        mineCount: Int,
    ): List<MineRow> {
        val positions =
            (RANDOM_MINE_START_VALUE until height).flatMap { row ->
                (RANDOM_MINE_START_VALUE until width).map { col -> row to col }
            }.shuffled()

        val minePositions = positions.take(mineCount).toSet()

        val board =
            (RANDOM_MINE_START_VALUE until height).map { row ->
                (RANDOM_MINE_START_VALUE until width).map { col ->
                    if (row to col in minePositions) MineCell.MINE else MineCell.initial()
                }
            }
        return BoardCalculator().calculateBoard(board.map { MineRow(it) })
    }

    companion object {
        private const val RANDOM_MINE_START_VALUE = 0
        const val DEFAULT_MINE_NUMBER = 0
    }
}
