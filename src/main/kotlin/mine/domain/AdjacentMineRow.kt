package mine.domain

import mine.domain.BoardCalculator.Companion.MINE_ADD_VALUE
import mine.domain.BoardCalculator.Companion.MINE_NORMAL_VALUE
import mine.domain.BoardCalculator.Companion.directions

data class AdjacentMineRow(
    val beforeRow: MineRow?,
    val currentRow: MineRow,
    val afterRow: MineRow?,
) {
    private fun validRows(): List<MineRow> = listOfNotNull(beforeRow, currentRow, afterRow)

    fun adjacentMineCalculate(col: Int): Int {
        return directions.sumOf { dx ->
            this.validRows().sumOf { row ->
                getCellValue(row, col + dx)
            }
        }
    }

    private fun getCellValue(
        row: MineRow,
        col: Int,
    ): Int {
        return if (row.isValidCell(col) && row.isMine(col)) {
            MINE_ADD_VALUE
        } else {
            MINE_NORMAL_VALUE
        }
    }
}
