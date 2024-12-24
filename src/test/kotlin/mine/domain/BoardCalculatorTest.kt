package mine.domain

import io.kotest.matchers.shouldBe
import mine.dto.Coordinate
import mine.enums.MineCell
import org.junit.jupiter.api.Test

class BoardCalculatorTest {
    @Test
    fun `좌표를 포함 주변 8칸의 셀이 모두 열린다`() {
        val mineBoard =
            listOf(
                MineRow(listOf(MineCell.MINE, MineCell.Number(1), MineCell.Number(1))),
                MineRow(listOf(MineCell.Number(2), MineCell.MINE, MineCell.Number(3))),
                MineRow(listOf(MineCell.MINE, MineCell.Number(4), MineCell.MINE)),
            )
        val coordinate = Coordinate(1, 0)
        val boardCalculator = BoardCalculator()
        boardCalculator.openCells(mineBoard, coordinate)
        mineBoard[0].mineCells[0].isOpen shouldBe false
        mineBoard[0].mineCells[1].isOpen shouldBe true
        mineBoard[1].mineCells[0].isOpen shouldBe true
        mineBoard[1].mineCells[1].isOpen shouldBe false
        mineBoard[2].mineCells[0].isOpen shouldBe false
        mineBoard[2].mineCells[1].isOpen shouldBe true
    }

    @Test
    fun `지뢰가 없는 영역에서 연쇄적으로 셀이 모두 열린다`() {
        val mineBoard =
            listOf(
                MineRow(listOf(MineCell.initial(), MineCell.initial(), MineCell.initial())),
                MineRow(listOf(MineCell.initial(), MineCell.initial(), MineCell.MINE)),
                MineRow(listOf(MineCell.initial(), MineCell.MINE, MineCell.initial())),
            )
        val coordinate = Coordinate(1, 1)
        val boardCalculator = BoardCalculator()

        boardCalculator.openCells(mineBoard, coordinate)

        mineBoard[0].mineCells[0].isOpen shouldBe true
        mineBoard[0].mineCells[1].isOpen shouldBe true
        mineBoard[0].mineCells[2].isOpen shouldBe true

        mineBoard[1].mineCells[0].isOpen shouldBe true
        mineBoard[1].mineCells[1].isOpen shouldBe true
        mineBoard[1].mineCells[2].isOpen shouldBe false

        mineBoard[2].mineCells[0].isOpen shouldBe true
        mineBoard[2].mineCells[1].isOpen shouldBe false
        mineBoard[2].mineCells[2].isOpen shouldBe true
    }

    @Test
    fun `인접셀 제외하고는 열리지 않는다`() {
        val mineBoard =
            listOf(
                MineRow(listOf(MineCell.MINE, MineCell.Number(1), MineCell.Number(1))),
                MineRow(listOf(MineCell.Number(2), MineCell.MINE, MineCell.Number(3))),
                MineRow(listOf(MineCell.MINE, MineCell.Number(4), MineCell.MINE)),
            )
        val coordinate = Coordinate(0, 0) // 중앙 셀 선택
        val boardCalculator = BoardCalculator()
        boardCalculator.openCells(mineBoard, coordinate)
        mineBoard[0].mineCells[2].isOpen shouldBe false
        mineBoard[1].mineCells[2].isOpen shouldBe false
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
