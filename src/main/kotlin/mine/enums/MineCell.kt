package mine.enums

import mine.domain.MineRandomPlacer.Companion.DEFAULT_MINE_NUMBER

sealed class MineCell {
    var isOpen: Boolean = false
        protected set

    data object MINE : MineCell() {
        override fun withOpen(): MineCell = openCell()
    }

    data class Number(val value: Int) : MineCell() {
        override fun withOpen(): MineCell = openCell()
    }

    abstract fun withOpen(): MineCell

    fun openCell(): MineCell = this.apply { isOpen = true }

    companion object {
        fun initial(): Number {
            return Number(DEFAULT_MINE_NUMBER)
        }
    }
}
