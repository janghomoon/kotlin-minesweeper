package mine.domain

import io.kotest.matchers.shouldBe
import mine.dto.Coordinate
import mine.enums.MineCell
import org.junit.jupiter.api.Test

class BoardCalculatorTest {
    @Test
    fun `선택 셀이 0 안전셀이면 인접셀이 모두 열린다`() {
        val mineBoard =
            listOf(
                MineRow(listOf(MineCell.initial(), MineCell.initial(), MineCell.initial())),
                MineRow(listOf(MineCell.initial(), MineCell.initial(), MineCell.initial())),
                MineRow(listOf(MineCell.initial(), MineCell.initial(), MineCell.initial())),
            )
        val coordinate = Coordinate(1, 1)
        val boardCalculator = BoardCalculator()
        boardCalculator.openCells(mineBoard, coordinate)
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
    fun `선택셀이  0이 아닐때(인접 폭탄) 해당셀만 열린다`() {
        val mineBoard =
            listOf(
                MineRow(listOf(MineCell.initial(), MineCell.initial(), MineCell.initial())),
                MineRow(listOf(MineCell.initial(), MineCell.Number(2), MineCell.MINE)),
                MineRow(listOf(MineCell.initial(), MineCell.MINE, MineCell.initial())),
            )
        val coordinate = Coordinate(1, 1)
        val boardCalculator = BoardCalculator()

        boardCalculator.openCells(mineBoard, coordinate)

        mineBoard[0].mineCells[0].isOpen shouldBe false
        mineBoard[0].mineCells[1].isOpen shouldBe false
        mineBoard[0].mineCells[2].isOpen shouldBe false
        mineBoard[1].mineCells[0].isOpen shouldBe false
        mineBoard[1].mineCells[1].isOpen shouldBe true
        mineBoard[1].mineCells[2].isOpen shouldBe false
        mineBoard[2].mineCells[0].isOpen shouldBe false
        mineBoard[2].mineCells[1].isOpen shouldBe false
        mineBoard[2].mineCells[2].isOpen shouldBe false
    }

    @Test
    fun `주변 지뢰갯수  확인후 셀 값 변경 지뢰 1개`() {
        val board =
            listOf(
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.initial(),
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.MINE,
                        MineCell.initial(),
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.initial(),
                    ),
                ),
            )
        val result = BoardCalculator().calculateBoard(board)
        val expectedResult =
            listOf(
                MineRow(listOf(MineCell.Number(1), MineCell.Number(1), MineCell.Number(1))),
                MineRow(listOf(MineCell.Number(1), MineCell.MINE, MineCell.Number(1))),
                MineRow(listOf(MineCell.Number(1), MineCell.Number(1), MineCell.Number(1))),
            )

        result shouldBe expectedResult
    }

    @Test
    fun `주변 지뢰갯수  확인후 셀 값 변경 지뢰 2개`() {
        val board =
            listOf(
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.initial(),
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.MINE,
                        MineCell.initial(),
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.MINE,
                        MineCell.initial(),
                    ),
                ),
            )
        val result = BoardCalculator().calculateBoard(board)
        val expectedResult =
            listOf(
                MineRow(listOf(MineCell.Number(1), MineCell.Number(1), MineCell.Number(1))),
                MineRow(listOf(MineCell.Number(2), MineCell.MINE, MineCell.Number(2))),
                MineRow(listOf(MineCell.Number(2), MineCell.MINE, MineCell.Number(2))),
            )

        result shouldBe expectedResult
    }

    @Test
    fun `주변 지뢰갯수  확인후 셀 값 변경 지뢰 0개`() {
        val board =
            listOf(
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.initial(),
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.initial(),
                    ),
                ),
                MineRow(
                    listOf(
                        MineCell.initial(),
                        MineCell.initial(),
                        MineCell.initial(),
                    ),
                ),
            )
        val result = BoardCalculator().calculateBoard(board)
        val expectedResult =
            listOf(
                MineRow(listOf(MineCell.Number(0), MineCell.Number(0), MineCell.Number(0))),
                MineRow(listOf(MineCell.Number(0), MineCell.Number(0), MineCell.Number(0))),
                MineRow(listOf(MineCell.Number(0), MineCell.Number(0), MineCell.Number(0))),
            )

        result shouldBe expectedResult
    }
}
