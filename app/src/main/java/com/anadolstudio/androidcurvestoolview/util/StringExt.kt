package com.anadolstudio.library.util

fun String.ifNotEmpty(action: () -> String): String = this.apply {
    if (this.isNotEmpty()) {
        plus(action.invoke())
    }
}

