package com.vitiligo.gradewise.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

fun Double.toFixed(limit: Int): String {
    return String.format("%.${limit}f", this)
}

fun Int.incrementIfLessThan(limit: Int): Int {
    return if (this < limit) this + 1 else this
}

fun Int.decrementIfGreaterThan(limit: Int): Int {
    return if (this > limit) this - 1 else this
}