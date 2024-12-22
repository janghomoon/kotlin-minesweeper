package mine.domain

import mine.dto.Coordinate
import mine.enums.MineCell

class BoardCalculator {
    fun isAllMinesOpened(mineBoard: List<MineRow>): Boolean {
        return mineBoard.all { row ->
            isMinesOpenedRow(row)
        }
    }

    private fun isMinesOpenedRow(row: MineRow) = row.mineCells.all { cell ->
        cell !is MineCell.MINE || cell.isOpen
    }

    fun openCells(mineBoard: List<MineRow>, coordinate: Coordinate) {
        mineBoard.forEachIndexed { rowIndex, row ->
            updateRows(row, coordinate, rowIndex)
        }
    }

    private fun updateRows(
        row: MineRow,
        coordinate: Coordinate,
        rowIndex: Int
    ) {
        row.mineCells.forEachIndexed { colIndex, cell ->
            updateCell(coordinate, rowIndex, colIndex, cell)
        }
    }

    private fun updateCell(
        coordinate: Coordinate,
        rowIndex: Int,
        colIndex: Int,
        cell: MineCell
    ) {
        if (isInRange(coordinate, rowIndex, colIndex)) {
            cell.isOpen = true
        }
    }

    private fun isInRange(coordinate: Coordinate, rowIndex: Int, colIndex: Int): Boolean {
        val rowInRange = rowIndex in (coordinate.y - CELL_POSITIVE)..(coordinate.y + CELL_POSITIVE)
        val colInRange = colIndex in (coordinate.x - CELL_POSITIVE)..(coordinate.x + CELL_POSITIVE)
        return rowInRange && colInRange
    }

    fun isMineCell(mineBoard: List<MineRow>, coordinate: Coordinate): Boolean {
        require(coordinate.x in mineBoard.indices) { "지뢰찾기 보드의 크기를 초과할 수 없습니다." }
        require(coordinate.y in mineBoard.first().mineCells.indices) { "지뢰찾기 보드의 크기를 초과할 수 없습니다." }
        return mineBoard[coordinate.y].isMine(coordinate.x)

    }

    fun calculateBoard(mineBoard: List<MineRow>): List<MineRow> {
        return mineBoard.mapIndexed { rowIndex, currentRow ->
            val beforeRow = mineBoard.getOrNull(rowIndex - 1)
            val afterRow = mineBoard.getOrNull(rowIndex + 1)
            calculateRow(currentRow, beforeRow, afterRow)
        }
    }

    private fun calculateRow(
        currentRow: MineRow,
        beforeRow: MineRow?,
        afterRow: MineRow?,
    ): MineRow {
        val updatedCells =
            currentRow.mineCells.mapIndexed { col, cell ->
                calculateCell(cell, beforeRow, currentRow, afterRow, col)
            }
        return MineRow(updatedCells)
    }

    private fun calculateCell(
        cell: MineCell,
        beforeRow: MineRow?,
        currentRow: MineRow,
        afterRow: MineRow?,
        col: Int,
    ): MineCell {
        return if (cell == MineCell.MINE) {
            MineCell.MINE
        } else {
            val calculatedMines = adjacentMineCalculate(beforeRow, currentRow, afterRow, col)
            MineCell.Number(calculatedMines)
        }
    }

    private fun adjacentMineCalculate(
        beforeRow: MineRow?,
        currentRow: MineRow,
        afterRow: MineRow?,
        col: Int,
    ): Int {
        return directions.sumOf { dx ->
            listOfNotNull(beforeRow, currentRow, afterRow).sumOf { row ->
                checkMine(row, col + dx)
            }
        }
    }

    private fun checkMine(
        row: MineRow,
        col: Int,
    ): Int {
        return if (row.isValidCell(col) && row.isMine(col)) {
            MINE_ADD_VALUE
        } else MINE_NORMAL_VALUE
    }

    companion object {
        private const val MINE_ADD_VALUE = 1
        private const val MINE_NORMAL_VALUE = 0
        const val CELL_NEGATIVE = -1
        const val CELL_ZERO = 0
        const val CELL_POSITIVE = 1
        private val directions =
            listOf(CELL_NEGATIVE, CELL_ZERO, CELL_POSITIVE)
    }
}
