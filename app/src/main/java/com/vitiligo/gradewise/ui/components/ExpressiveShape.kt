package com.vitiligo.gradewise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vitiligo.gradewise.ui.theme.GradeWiseTheme

@Composable
fun ExpressiveShape(
    size: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .background(color)
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true)
@Composable
fun ExpressiveShapePreview() {
    GradeWiseTheme {
        ExpressiveShape(
            size = 100.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clip(MaterialShapes.Square.toShape())
        )
    }
}