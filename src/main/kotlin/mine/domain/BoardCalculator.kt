package mine.domain

import mine.dto.Coordinate
import mine.enums.MineCell

class BoardCalculator {
    fun isMineCell(
        mineBoard: List<MineRow>,
        coordinate: Coordinate,
    ): Boolean {
        require(coordinate.y in mineBoard.indices) { "지뢰찾기 보드의 크기를 초과할 수 없습니다." }
        require(coordinate.x in mineBoard.first().mineCells.indices) { "지뢰찾기 보드의 크기를 초과할 수 없습니다." }
        return mineBoard[coordinate.y].isMine(coordinate.x)
    }

    fun calculateBoard(mineBoard: List<MineRow>): List<MineRow> {
        return mineBoard.mapIndexed { rowIndex, currentRow ->
            val beforeRow = mineBoard.getOrNull(rowIndex - 1)
            val afterRow = mineBoard.getOrNull(rowIndex + 1)

            calculateRow(
                AdjacentMineRow(
                    currentRow = currentRow,
                    beforeRow = beforeRow,
                    afterRow = afterRow,
                ),
            )
        }
    }

    private fun calculateRow(adjacentMineRow: AdjacentMineRow): MineRow {
        val updatedCells =
            adjacentMineRow.currentRow.mineCells.mapIndexed { col, cell ->
                calculateCell(cell, adjacentMineRow, col)
            }
        return MineRow(updatedCells)
    }

    private fun calculateCell(
        cell: MineCell,
        adjacentMineRow: AdjacentMineRow,
        col: Int,
    ): MineCell {
        return when (cell) {
            is MineCell.MINE -> MineCell.MINE
            is MineCell.Number -> {
                val calculatedMines = adjacentMineRow.adjacentMineCalculate(col)
                cell.copy(value = calculatedMines)
            }
        }
    }

    companion object {
        const val MINE_ADD_VALUE = 1
        const val MINE_NORMAL_VALUE = 0
        const val CELL_NEGATIVE = -1
        const val CELL_ZERO = 0
        const val CELL_POSITIVE = 1
        val directions =
            listOf(CELL_NEGATIVE, CELL_ZERO, CELL_POSITIVE)
    }
}
