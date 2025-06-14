package com.vitiligo.gradewise.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitiligo.gradewise.model.Semester
import com.vitiligo.gradewise.model.SemesterInfo
import com.vitiligo.gradewise.model.toSemester
import com.vitiligo.gradewise.ui.theme.GradeWiseTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SemesterList(
    loading: Boolean,
    semesters: List<SemesterInfo>,
    onSemesterClick: (String, String) -> Unit,
    deleteSemester: (Semester) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Semesters",
            style = MaterialTheme.typography.displaySmallEmphasized,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (loading) {
            LoadingIndicator(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                size = 108.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .height(350.dp)
            )
        } else {
            repeat(semesters.size) {
                SemesterCard(
                    semester = semesters[it],
                    deleteSemester = { deleteSemester(semesters[it].toSemester()) },
                    modifier = Modifier
                        .clickable { onSemesterClick(semesters[it].id, semesters[it].name) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (semesters.isEmpty()) {
                EmptyListMessage(
                    label = "semester",
                    modifier = Modifier
                        .padding(top = 56.dp)
                )
            }
        }
    }
}

@Composable
fun SemesterCard(
    semester: SemesterInfo,
    deleteSemester: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = semester.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    IconWithLabel(
                        text = "${semester.numCourses} Course${if (semester.numCourses > 1) 's' else ""}",
                        icon = Icons.AutoMirrored.Filled.Article
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    IconWithLabel(
                        text = "${semester.totalCreditHours} Credit Hour${if (semester.totalCreditHours > 1) 's' else ""}",
                        icon = Icons.Filled.Numbers
                    )
                }
            }
            SemesterDropdown(
                deleteSemester = deleteSemester,
                modifier = Modifier
                    .align(Alignment.Top)
            )
        }
    }
}

@Composable
fun SemesterDropdown(
    deleteSemester: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .wrapContentSize()
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
            IconButton(
                onClick = {
                    expanded = false
                },
                modifier = Modifier
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { expanded = !expanded }
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    deleteSemester()
                },
                text = { Text("Delete Semester") },
                leadingIcon = { Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                ) }
            )
        }
    }
}

@Composable
fun IconWithLabel(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = false, showSystemUi = false
)
@Composable
fun SemesterCardPreview() {
    GradeWiseTheme {
        SemesterCard(
            semester = SemesterInfo("a", "Fall 2023", 6, 18),
            deleteSemester = { },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}