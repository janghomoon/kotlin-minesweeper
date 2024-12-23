package mine.enums

import mine.domain.MineRandomPlacer.Companion.DEFAULT_MINE_NUMBER

sealed class MineCell {
    private var _isOpen: Boolean = false
    val isOpen: Boolean
        get() = _isOpen

    protected fun setOpen() {
        _isOpen = true
    }

    data object MINE : MineCell() {
        override fun withOpen(): MineCell = this.apply { setOpen() }
    }

    data class Number(val value: Int) : MineCell() {
        override fun withOpen(): MineCell = this.apply { setOpen() }
    }

    abstract fun withOpen(): MineCell

    companion object {
        fun initial(): Number {
            return Number(DEFAULT_MINE_NUMBER)
        }
    }
}
