package mine.domain

import mine.dto.Coordinate

data class UpdateMineRow(
    val row: MineRow,
    val coordinate: Coordinate,
    val rowIndex: Int,
) {
    fun updateCells() {
        row.updateCellsInRow(coordinate, rowIndex)
    }
}
