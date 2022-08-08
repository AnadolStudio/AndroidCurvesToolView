package com.anadolstudio.library.curvestool.util

inline fun <T> List<T>.forEachWithPrevious(action: (T?, T) -> Unit) {
    var previous: T? = null

    for (item in this) {
        action(previous, item)
        previous = item
    }
}
