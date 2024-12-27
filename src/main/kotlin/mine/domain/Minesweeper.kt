package mine.domain

import mine.domain.BoardCalculator.Companion.CELL_NEGATIVE
import mine.domain.BoardCalculator.Companion.CELL_POSITIVE
import mine.domain.BoardCalculator.Companion.CELL_ZERO
import mine.dto.Coordinate
import mine.enums.MineCell

data class Minesweeper(
    val height: Int,
    val width: Int,
    val mineCount: Int,
    val mineBoard: List<MineRow> =
        MineRandomPlacer().placeMines(
            height,
            width,
            mineCount,
        ),
) {
    init {
        require(height > MINE_MIN_VALUE) { "높이는 0보다 커야합니다." }
        require(width > MINE_MIN_VALUE) { "너비는 0보다 커야합니다." }
        require(mineCount > MINE_MIN_VALUE) { "지뢰 개수는 0보다 커야합니다." }
        require(mineCount <= height * width) { "지뢰 개수는 전체 칸의 수보다 많을 수 없습니다." }
    }

    fun areAllSafeCellsOpened(): Boolean {
        return this.mineBoard.all { row ->
            areRowSafeCellsOpened(row)
        }
    }

    fun openCells(coordinate: Coordinate) {
        val (x, y) = coordinate
        val cell = mineBoard[x].mineCells[y]
        if (isOpen(cell)) return
        cell.withOpen()
        when (cell) {
            is MineCell.Number -> {
                selectedCellIfSafety(cell, coordinate)
            }

            else -> return
        }
    }

    private fun selectedCellIfSafety(
        cell: MineCell.Number,
        coordinate: Coordinate,
    ) {
        val (x, y) = coordinate
        if (cell.value == CELL_ZERO) {
            val getNeighbors = getNeighbors(x, y)
            getNeighbors.forEach { neighbor ->
                handleCell(neighbor.first, neighbor.second)
            }
        }
    }

    private fun handleCell(
        rowIndex: Int,
        colIndex: Int,
    ) {
        val currentCell = mineBoard[rowIndex].mineCells[colIndex]
        when {
            isMine(currentCell) -> return
            isOpen(currentCell) -> return
            currentCell is MineCell.Number -> {
                openCell(currentCell)
            }

            else -> return
        }
    }

    private fun openCell(currentCell: MineCell.Number) {
        if (currentCell.value == CELL_ZERO) {
            currentCell.withOpen()
        }
    }

    private fun isOpen(currentCell: MineCell) = currentCell.isOpen

    private fun isMine(currentCell: MineCell) = currentCell is MineCell.MINE

    private fun getNeighbors(
        rowIndex: Int,
        colIndex: Int,
    ): List<Pair<Int, Int>> {
        return directions.map { (dx, dy) -> rowIndex + dx to colIndex + dy }
            .filter { (row, col) ->
                row in mineBoard.indices && col in mineBoard[row].mineCells.indices
            }
    }

    private fun areRowSafeCellsOpened(row: MineRow) = row.areAllNonMineCellsOpen()

    companion object {
        const val MINE_MIN_VALUE = 0
        private val directions =
            listOf(
                CELL_NEGATIVE to CELL_NEGATIVE,
                CELL_NEGATIVE to CELL_ZERO,
                CELL_NEGATIVE to CELL_POSITIVE,
                CELL_ZERO to CELL_NEGATIVE,
                CELL_ZERO to CELL_POSITIVE,
                CELL_POSITIVE to CELL_NEGATIVE,
                CELL_POSITIVE to CELL_ZERO,
                CELL_POSITIVE to CELL_POSITIVE,
            )
    }
}
