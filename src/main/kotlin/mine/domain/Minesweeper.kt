package mine.domain

import mine.domain.BoardCalculator.Companion.CELL_ZERO
import mine.dto.Coordinate
import mine.enums.MineCell

data class Minesweeper(val height: Int, val width: Int, val mineCount: Int) {
    val mineBoard: List<MineRow>

    init {
        require(height > MINE_MIN_VALUE) { "높이는 0보다 커야합니다." }
        require(width > MINE_MIN_VALUE) { "너비는 0보다 커야합니다." }
        require(mineCount > MINE_MIN_VALUE) { "지뢰 개수는 0보다 커야합니다." }
        require(mineCount <= height * width) { "지뢰 개수는 전체 칸의 수보다 많을 수 없습니다." }
        mineBoard = MineRandomPlacer().placeMines(height, width, mineCount)
    }

    fun areAllSafeCellsOpened(): Boolean {
        return this.mineBoard.all { row ->
            areRowSafeCellsOpened(row)
        }
    }
    fun openCells(
        coordinate: Coordinate
    ) {
        val (x, y) = coordinate
        val cell = mineBoard[x].mineCells[y]
        if (cell.isOpen) return
        cell.withOpen()
        when (cell) {
            is MineCell.Number -> {
                selectedCellIfSafety(cell, mineBoard, coordinate)
            }

            else -> return
        }
    }

    private fun selectedCellIfSafety(
        cell: MineCell.Number,
        mineBoard: List<MineRow>,
        coordinate: Coordinate,
    ) {
        if (cell.value == CELL_ZERO) {
            mineBoard.forEachIndexed { rowIndex, row ->
                UpdateMineRow(row = row, coordinate = coordinate, rowIndex = rowIndex).updateCells()
            }
        }
    }

    private fun areRowSafeCellsOpened(row: MineRow) = row.areAllNonMineCellsOpen()

    companion object {
        const val MINE_MIN_VALUE = 0
    }
}
