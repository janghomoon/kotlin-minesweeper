package mine.domain

import mine.domain.BoardCalculator.Companion.CELL_POSITIVE
import mine.dto.Coordinate
import mine.enums.MineCell
import mine.enums.MineCell.MINE

data class MineRow(val mineCells: List<MineCell>) {
    fun isValidCell(col: Int): Boolean {
        return col in this.mineCells.indices
    }

    fun isMine(index: Int): Boolean {
        return mineCells.getOrNull(index) == MINE
    }
    private fun isNotMine(cell: MineCell): Boolean {
        return cell != MINE
    }

    fun isAllOpen() = this.mineCells.all { cell -> cell !is MINE || cell.isOpen }

    fun updateCellsInRow(
        coordinate: Coordinate,
        rowIndex: Int,
    ) {
        this.mineCells.forEachIndexed { colIndex, cell ->
            openCellIfInRangeAndNotMine(coordinate, rowIndex, colIndex, cell)
        }
    }

    private fun openCellIfInRangeAndNotMine(
        coordinate: Coordinate,
        rowIndex: Int,
        colIndex: Int,
        cell: MineCell,
    ) {
        if (isInRange(coordinate, rowIndex, colIndex) && isNotMine(cell)) {
            cell.withOpen()
        }
    }

    private fun isInRange(
        coordinate: Coordinate,
        rowIndex: Int,
        colIndex: Int,
    ): Boolean {
        val rowInRange = rowIndex in (coordinate.x - CELL_POSITIVE)..(coordinate.x + CELL_POSITIVE)
        val colInRange = colIndex in (coordinate.y - CELL_POSITIVE)..(coordinate.y + CELL_POSITIVE)

        return rowInRange && colInRange
    }
}
