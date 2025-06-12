package com.vitiligo.gradewise.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vitiligo.gradewise.model.Course
import com.vitiligo.gradewise.ui.components.BottomOutlineTextField
import com.vitiligo.gradewise.ui.components.CourseCard
import com.vitiligo.gradewise.ui.components.GPACard
import com.vitiligo.gradewise.ui.utils.ScrollDirection
import com.vitiligo.gradewise.ui.utils.rememberDirectionalLazyListState
import com.vitiligo.gradewise.ui.viewmodels.SemesterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterScreen(
    semesterId: Int,
    semesterName: String,
    navigateBack: () -> Unit,
    viewModel: SemesterViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val lazyListState = rememberLazyListState()
    val directionalLazyListState = rememberDirectionalLazyListState(
        lazyListState
    )
    val isVisible by remember {
        derivedStateOf {
            directionalLazyListState.scrollDirection == ScrollDirection.None || directionalLazyListState.scrollDirection == ScrollDirection.Up
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SemesterScreenAppBar(
                title = uiState.semesterName,
                navigateBack = navigateBack,
                scrollBehavior = scrollBehavior,
                updateSemesterName = viewModel::updateSemesterName
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 })
            ) {
                ExtendedFloatingActionButton(
                    onClick = viewModel::addCourseForSemester,
                    icon = { Icon(Icons.Filled.Add, "Add Course") },
                    text = { Text("Add Course") }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = Modifier
    ) {
        SemesterScreenBody(
            lazyListState = lazyListState,
            sgpa = uiState.sgpa,
            courses = uiState.courses,
            updateCourse = viewModel::updateCourseForSemester,
            deleteCourse = viewModel::deleteCourseFromSemester,
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SemesterScreenBody(
    lazyListState: LazyListState,
    sgpa: Double,
    courses: List<Course>,
    updateCourse: (Course) -> Unit,
    deleteCourse: (Course) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = lazyListState,
        modifier = modifier
    ) {
        item {
            SemesterGPACard(
                sgpa = sgpa,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }
        item {
            Text(
                text = "Courses",
                style = MaterialTheme.typography.displaySmallEmphasized,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            )
        }
        items(courses) {
            CourseCard(
                course = it,
                updateCourse = updateCourse,
                deleteCourse = deleteCourse
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Spacer(modifier = Modifier.height(74.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SemesterGPACard(
    sgpa: Double,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            GPACard(
                label = "SGPA",
                labelInBox = false,
                gpa = sgpa,
                gpaTextSize = 72.sp,
                outOfTextStyle = MaterialTheme.typography.displaySmallEmphasized,
                outOfTextOffset = (-10).dp,
                modifier = Modifier,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterScreenAppBar(
    title: String,
    navigateBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    updateSemesterName: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    var tempText by remember { mutableStateOf(title) }

    BackHandler {
        if (isEditing) {
            isEditing = false
            tempText = title
        }
        else navigateBack()
    }

    MediumTopAppBar(
        title = {
            val collapsed = scrollBehavior.state.collapsedFraction > 0.5f

            if (collapsed) {
                Text(text = title)
            } else {
                EditSemesterTitle(
                    isEditing = isEditing,
                    updateSemesterName = updateSemesterName,
                    setIsEditing = { isEditing = it },
                    tempText = tempText,
                    setTempText = { tempText = it },
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun EditSemesterTitle(
    isEditing: Boolean,
    tempText: String,
    setTempText: (String) -> Unit,
    updateSemesterName: (String) -> Unit,
    setIsEditing: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        BottomOutlineTextField(
            value = tempText,
            onValueChange = { setTempText(it) },
            textStyle = MaterialTheme.typography.titleLargeEmphasized.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            enabled = isEditing,
            modifier = Modifier
                .weight(1f),
        )

        AnimatedContent(
            targetState = isEditing,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "EditToggle"
        ) { editing ->
            IconButton(
                onClick = {
                    setIsEditing(!editing)
                    if (!isEditing) {
                        updateSemesterName(tempText)
                    }
                },
                colors = IconButtonDefaults.outlinedIconButtonColors(),
                modifier = Modifier
                    .padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = if (editing) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = if (editing) "Save" else "Edit",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
