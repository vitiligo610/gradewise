package com.vitiligo.gradewise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SegmentedProgressBar(
    segments: Int,
    progress: Int,
    modifier: Modifier = Modifier,
    completedColor: Color = MaterialTheme.colorScheme.primary,
    incompleteColor: Color = MaterialTheme.colorScheme.surfaceDim,
    height: Dp = 16.dp,
    segmentCornerSize: Dp = 2.dp,
    firstLastSegmentCornerSize: Dp = 12.dp,
    spaceBetweenSegments: Dp = 2.dp,
) {
    require(segments >= 2) { "segments must be equal to or greater than 2" }
    require(progress in 0..segments) { "progress must be less than or equal to segments" }

    Row(
        horizontalArrangement = Arrangement.spacedBy(spaceBetweenSegments),
        modifier = modifier
            .height(height),
    ) {
        for (index in 0 until segments) {
            val shape = when (index) {
                0 -> RoundedCornerShape(
                    topStart = firstLastSegmentCornerSize,
                    topEnd = segmentCornerSize,
                    bottomStart = firstLastSegmentCornerSize,
                    bottomEnd = segmentCornerSize,
                )

                segments - 1 -> RoundedCornerShape(
                    topStart = segmentCornerSize,
                    topEnd = firstLastSegmentCornerSize,
                    bottomStart = segmentCornerSize,
                    bottomEnd = firstLastSegmentCornerSize,
                )

                else -> RoundedCornerShape(segmentCornerSize)
            }

            val color = if (index < progress) completedColor else incompleteColor

            Box(
                modifier = Modifier
                    .clip(shape)
                    .background(color)
                    .fillMaxHeight()
                    .weight(1f),
            )
        }
    }
}