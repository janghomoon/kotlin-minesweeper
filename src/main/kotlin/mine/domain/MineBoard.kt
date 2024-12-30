package mine.domain

import mine.domain.BoardCalculator.Companion.CELL_NEGATIVE
import mine.domain.BoardCalculator.Companion.CELL_POSITIVE
import mine.domain.BoardCalculator.Companion.CELL_ZERO
import mine.dto.Coordinate
import mine.enums.MineCell

class MineBoard(val rows: List<MineRow>) {
    fun isMine(coordinate: Coordinate): Boolean {
        val (rowIndex, colIndex) = coordinate
        return rows.getOrNull(rowIndex)?.mineCells?.getOrNull(colIndex) is MineCell.MINE
    }

    fun openCells(coordinate: Coordinate) {
        val visited = mutableSetOf<Coordinate>()
        val queue = ArrayDeque<Coordinate>()
        queue.add(coordinate)

        while (queue.isNotEmpty()) {
            val (x, y) = queue.removeFirst()
            val currentCell = getCell(Coordinate(x, y)) ?: continue

            if (currentCell.isOpen || visited.contains(Coordinate(x, y))) continue
            visited.add(Coordinate(x, y))

            if (currentCell is MineCell.MINE) continue

            currentCell.withOpen()
            if (currentCell is MineCell.Number && currentCell.value == MINE_MIN_VALUE) {
                val neighbors = getNeighbors(Coordinate(x, y))
                neighbors.filterNot { it in visited }.forEach(queue::add)
            }
        }
    }

    fun areAllSafeCellsOpened(): Boolean {
        return rows.all { it.areAllNonMineCellsOpen() }
    }

    fun getCell(coordinate: Coordinate): MineCell? {
        return rows.getOrNull(coordinate.x)?.mineCells?.getOrNull(coordinate.y)
    }

    fun getNeighbors(coordinate: Coordinate): List<Coordinate> {
        return directions.map { (dx, dy) ->
            Coordinate(coordinate.x + dx, coordinate.y + dy)
        }.filter { (x, y) ->
            x in rows.indices && y in rows[0].mineCells.indices
        }
    }

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
