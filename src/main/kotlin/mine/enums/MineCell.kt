package mine.enums

sealed class MineCell(var isOpen: Boolean = false) {
    data object MINE : MineCell() {
        override fun withOpen(isOpen: Boolean): MineCell = MINE.apply { this.isOpen = isOpen }
    }

    data class Number(val value: Int) : MineCell() {
        override fun withOpen(isOpen: Boolean): MineCell = copy().apply { this.isOpen = isOpen }
    }

    abstract fun withOpen(isOpen: Boolean): MineCell
}
