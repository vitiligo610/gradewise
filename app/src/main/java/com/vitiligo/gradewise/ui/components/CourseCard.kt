package com.vitiligo.gradewise.ui.components

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitiligo.gradewise.model.Course
import com.vitiligo.gradewise.model.Grade
import com.vitiligo.gradewise.model.dec
import com.vitiligo.gradewise.model.inc
import com.vitiligo.gradewise.ui.theme.GradeWiseTheme
import com.vitiligo.gradewise.ui.utils.decrementIfGreaterThan
import com.vitiligo.gradewise.ui.utils.incrementIfLessThan

@Composable
fun CourseCard(
    course: Course,
    updateCourse: (Course) -> Unit,
    deleteCourse: (Course) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
        ),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            BottomOutlineTextField(
                value = course.name,
                onValueChange = { updateCourse(course.copy(name = it)) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                space = 8.dp,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            )
            IconButton(
                onClick = { deleteCourse(course) },
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        ProgressComponent(
            label = "Credits: ",
            value = course.creditHours.toString(),
            segments = 6,
            progress = course.creditHours,
            onPlusClick = { updateCourse(course.copy(creditHours = course.creditHours.incrementIfLessThan(6))) },
            onMinusClick = { updateCourse(course.copy(creditHours = course.creditHours.decrementIfGreaterThan(1))) },
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
        ProgressComponent(
            label = "Grade: ",
            value = course.grade.value,
            segments = 8,
            progress = course.grade.order,
            onPlusClick = { updateCourse(course.copy(grade = course.grade.inc())) },
            onMinusClick = { updateCourse(course.copy(grade = course.grade.dec())) },
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProgressComponent(
    label: String,
    value: String,
    segments: Int,
    progress: Int,
    onPlusClick: () -> Unit,
    onMinusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = onMinusClick,
                enabled = progress > 1
            ) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            SegmentedProgressBar(
                segments = segments,
                progress = progress,
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize()
            )
            IconButton(
                onClick = onPlusClick,
                enabled = progress < segments
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CourseCardPreview() {
    GradeWiseTheme {
        CourseCard(
            course = Course(1, 1, "Course # 1", 3, Grade.A),
            updateCourse = { },
            deleteCourse = { },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}