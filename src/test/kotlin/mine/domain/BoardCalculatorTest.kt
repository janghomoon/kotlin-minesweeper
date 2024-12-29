package mine.domain

import io.kotest.matchers.shouldBe
import mine.enums.MineCell
import org.junit.jupiter.api.Test

class BoardCalculatorTest {
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
