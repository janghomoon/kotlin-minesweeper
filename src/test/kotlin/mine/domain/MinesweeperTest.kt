package mine.domain

import io.kotest.matchers.shouldBe
import mine.dto.Coordinate
import mine.enums.MineCell
import mine.view.OutputView
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MinesweeperTest {
    @Test
    fun `인접셀이 폭탄이 있는경우 확장되지 않는다`() {
        val tempBoard =
            listOf(
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.Number(1),
                        MineCell.Number(1),
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.Number(1),
                        MineCell.Number(2),
                        MineCell.MINE,
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.Number(1),
                        MineCell.MINE,
                        MineCell.Number(2),
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.Number(1),
                        MineCell.Number(2),
                        MineCell.MINE,
                        MineCell.Number(2),
                        MineCell.Number(1),
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.Number(1),
                        MineCell.MINE,
                        MineCell.Number(2),
                        MineCell.Number(1),
                        MineCell.initial(),
                    ),
                ),
            )
        val minesweeper =
            Minesweeper(
                height = 5,
                width = 5,
                mineCount = 4,
                mineBoard = tempBoard,
            )

        val coordinate = Coordinate(1, 1)

        minesweeper.openCells(coordinate)

        val resultMineBoard = minesweeper.mineBoard
        OutputView.gameResult(resultMineBoard)

        (targetRange(0, 3)).forEach { row ->
            val (start, end) =
                when {
                    row <= 1 -> 0 to 3
                    row == 2 -> 0 to 2
                    else -> 0 to 1
                }
            (targetRange(start, end)).forEach { col ->
                resultMineBoard[row].mineCells[col].isOpen shouldBe true
            }
        }

        // close Cell
        (targetRange(0, 4)).forEach { row ->
            val (start, end) =
                when {
                    row <= 1 -> 4 to 4
                    row == 2 -> 3 to 4
                    row == 3 -> 2 to 4
                    else -> 0 to 4
                }

            (targetRange(start, end)).forEach { col ->
                resultMineBoard[row].mineCells[col].isOpen shouldBe false
            }
        }
    }

    private fun targetRange(
        start: Int,
        end: Int,
    ) = start..end

    @Test
    fun `선택셀이  0이 아닐때(인접 폭탄) 해당셀만 열린다`() {
        val tempBoard =
            listOf(
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.Number(1),
                        MineCell.Number(1),
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.Number(1),
                        MineCell.Number(2),
                        MineCell.MINE,
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.Number(1),
                        MineCell.MINE,
                        MineCell.Number(2),
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.Number(1),
                        MineCell.Number(2),
                        MineCell.MINE,
                        MineCell.Number(2),
                        MineCell.Number(1),
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.Number(1),
                        MineCell.MINE,
                        MineCell.Number(2),
                        MineCell.Number(1),
                        MineCell.initial(),
                    ),
                ),
            )
        val minesweeper =
            Minesweeper(
                height = 5,
                width = 5,
                mineCount = 4,
                mineBoard = tempBoard,
            )

        val coordinate = Coordinate(4, 0)

        minesweeper.openCells(coordinate)

        val resultMineBoard = minesweeper.mineBoard
        val closedCellCount =
            resultMineBoard.sumOf { row ->
                row.mineCells.count { cell -> !cell.isOpen }
            }
        closedCellCount shouldBe 24
    }

    @Test
    fun `선택 셀이 0 안전셀이면 인접셀이 모두 열린다`() {
        val mineBoard =
            listOf(
                MineRow(listOf(MineCell.initial(), MineCell.initial(), MineCell.initial())),
                MineRow(listOf(MineCell.initial(), MineCell.initial(), MineCell.initial())),
                MineRow(listOf(MineCell.initial(), MineCell.initial(), MineCell.initial())),
            )
        val coordinate = Coordinate(1, 1)
        val minesweeper =
            Minesweeper(
                height = 3,
                width = 3,
                mineCount = 1,
                mineBoard = mineBoard,
            )
        minesweeper.openCells(coordinate)
        mineBoard[0].mineCells[0].isOpen shouldBe true
        mineBoard[0].mineCells[1].isOpen shouldBe true
        mineBoard[0].mineCells[2].isOpen shouldBe true
        mineBoard[1].mineCells[0].isOpen shouldBe true
        mineBoard[1].mineCells[1].isOpen shouldBe true
        mineBoard[1].mineCells[2].isOpen shouldBe true
        mineBoard[2].mineCells[0].isOpen shouldBe true
        mineBoard[2].mineCells[1].isOpen shouldBe true
        mineBoard[2].mineCells[2].isOpen shouldBe true
    }

    @Test
    fun `지뢰 필드 생성 테스트`() {
        val minesweeper = Minesweeper(5, 5, 3)
        minesweeper.mineBoard.size shouldBe 5
        minesweeper.mineBoard[0].mineCells.size shouldBe 5
    }

    @Test
    fun `지뢰 생성 개수 테스트`() {
        val height = 5
        val width = 5
        val mineCount = 10
        val minesweeper = Minesweeper(height, width, mineCount)

        val placedMineCount =
            minesweeper.mineBoard.sumOf { row -> row.mineCells.count { it == MineCell.MINE } }
        mineCount shouldBe placedMineCount
    }

    @Test
    fun `지뢰 개수가 게임 크기 초과시 예외`() {
        val height = 3
        val width = 3
        val mineCount = 10

        val exception =
            assertThrows<IllegalArgumentException> {
                Minesweeper(height, width, mineCount)
            }
        exception.message shouldBe "지뢰 개수는 전체 칸의 수보다 많을 수 없습니다."
    }

    @Test
    fun `파라메터  0보다큰 양수 확인 `() {
        val exception1 = assertThrows<IllegalArgumentException> { Minesweeper(0, 5, 3) }
        exception1.message shouldBe "높이는 0보다 커야합니다."

        val exception2 = assertThrows<IllegalArgumentException> { Minesweeper(5, 0, 3) }
        exception2.message shouldBe "너비는 0보다 커야합니다."

        val exception3 = assertThrows<IllegalArgumentException> { Minesweeper(5, 5, 0) }
        exception3.message shouldBe "지뢰 개수는 0보다 커야합니다."
    }
}
