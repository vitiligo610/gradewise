package com.vitiligo.gradewise.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

object Utils {
    fun roundOffDecimal(number: Double): String {
        return String.format("%.2f", number)
    }
}

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

fun Int.incrementIfLessThan(limit: Int): Int {
    return if (this < limit) this + 1 else this
}

fun Int.decrementIfGreaterThan(limit: Int): Int {
    return if (this > limit) this - 1 else this
}