package com.n27.core.extensions

import java.math.RoundingMode

fun Int.toStringId() = if (this < 10)
    "0$this"
else
    this.toString()

fun Int.divide(divisor: Int) : String {
    val percentage = (toDouble() / divisor) * 100
    return percentage.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toString()
}