package com.anadolstudio.library.curvestool.util

inline fun <T> List<T>.forEachWithPreviousAndNext(action: (T, T, T, T) -> Unit) {
    if (isEmpty()) return

    var prevPrevious: T = this.first()
    var previous: T = this.first()

    for ((index, current) in this.withIndex()) {
        val next = this.nextOrNull(index) ?: current

        action(prevPrevious, previous, current, next)

        prevPrevious = previous
        previous = current
    }
}

fun <T> List<T>.nextOrNull(current: Int) = if (current < this.lastIndex) this[current + 1] else null
