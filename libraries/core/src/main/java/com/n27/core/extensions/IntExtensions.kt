package com.n27.core.extensions

fun Int.toStringId() = if (this < 10)
    "0$this"
else
    this.toString()
