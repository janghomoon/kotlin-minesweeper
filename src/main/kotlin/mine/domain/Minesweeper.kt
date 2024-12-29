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
        val visited = mutableSetOf<Coordinate>()
        val queue = ArrayDeque<Coordinate>()
        queue.add(coordinate)

        while (queue.isNotEmpty()) {
            val (x, y) = queue.removeFirst()
            val currentCell = mineBoard[x].mineCells[y]
            if (isOpenedCellOrVisitedCell(currentCell, x, y, visited)) continue
            visited.add(Coordinate(x, y))

            if (isMine(currentCell)) continue

            openCell(currentCell, x, y, visited, queue)
        }
    }

    private fun openCell(
        currentCell: MineCell,
        x: Int,
        y: Int,
        visited: MutableSet<Coordinate>,
        queue: ArrayDeque<Coordinate>,
    ) {
        if (currentCell is MineCell.Number) {
            currentCell.withOpen()
            if (isCellGreaterThanZero(currentCell)) return
            val neighbors = getNeighbors(x, y)

            addNeighborsToQueueIfUnvisited(neighbors, visited, queue)
        }
    }

    private fun addNeighborsToQueueIfUnvisited(
        neighbors: List<Coordinate>,
        visited: MutableSet<Coordinate>,
        queue: ArrayDeque<Coordinate>,
    ) {
        neighbors.forEach { (neighborRow, neighborCol) ->
            val neighborCoordinate = Coordinate(neighborRow, neighborCol)
            if (neighborCoordinate !in visited) queue.add(neighborCoordinate)
        }
    }

    private fun isCellGreaterThanZero(currentCell: MineCell.Number): Boolean {
        return currentCell.value > CELL_ZERO
    }

    private fun isMine(currentCell: MineCell): Boolean {
        return currentCell is MineCell.MINE
    }

    private fun isOpenedCellOrVisitedCell(
        currentCell: MineCell,
        x: Int,
        y: Int,
        visited: MutableSet<Coordinate>,
    ): Boolean {
        return currentCell.isOpen || Coordinate(x, y) in visited
    }

    private fun getNeighbors(
        rowIndex: Int,
        colIndex: Int,
    ): List<Coordinate> {
        return directions.map { (dx, dy) -> Coordinate(rowIndex + dx, colIndex + dy) }
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
