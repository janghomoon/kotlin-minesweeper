package mine.domain

import mine.enums.MineCell
import mine.enums.MineCell.MINE

data class MineRow(val mineCells: List<MineCell>) {
    fun isValidCell(col: Int): Boolean {
        return col in this.mineCells.indices
    }

    fun isMine(index: Int): Boolean {
        return mineCells.getOrNull(index) == MINE
    }

    fun areAllNonMineCellsOpen() = this.mineCells.all { cell -> cell !is MINE || cell.isOpen }
}
