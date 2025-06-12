package com.vitiligo.gradewise.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitiligo.gradewise.ui.theme.GradeWiseTheme
import com.vitiligo.gradewise.ui.utils.toFixed

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CGPACard(
    cgpa: Double,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(250.dp)
            .fillMaxSize()
    ) {
        ExpressiveShape(
            size = 130.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.16f),
            modifier = Modifier
                .clip(MaterialShapes.Sunny.toShape())
                .align(BiasAlignment(1.4f, -0.7f))
        )
        GPACard(
            gpa = cgpa
        )
        ExpressiveShape(
            size = 140.dp,
            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.08f),
            modifier = Modifier
                .clip(MaterialShapes.Pentagon.toShape())
                .align(BiasAlignment(-1.45f, 1.1f))
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GPACard(
    modifier: Modifier = Modifier,
    label: String = "Cumulative GPA",
    labelInBox: Boolean = true,
    gpa: Double = 0.0,
    gpaTextColor: Color = MaterialTheme.colorScheme.primary,
    gpaTextSize: TextUnit = 96.sp,
    gpaTextFontWeight: FontWeight = FontWeight.Medium,
    outOf: Double = 4.00,
    outOfTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    outOfTextStyle: TextStyle = MaterialTheme.typography.displayMediumEmphasized,
    outOfTextOffset: Dp = (-15).dp,
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .then(
                    if (labelInBox) {
                        Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                            .padding(horizontal = 16.dp, 4.dp)
                    } else {
                        Modifier
                    }
                )
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = gpa.toFixed(2),
                color = gpaTextColor,
                fontSize = gpaTextSize,
                fontWeight = gpaTextFontWeight,
            )
            Text(
                text = "/${outOf.toFixed(2)}",
                color = outOfTextColor,
                style = outOfTextStyle,
                modifier = Modifier
                    .offset(y = outOfTextOffset)
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun CGPASectionPreview() {
    GradeWiseTheme {
        Surface {
            CGPACard(
                cgpa = 3.88
            )
        }
    }
}