package com.vitiligo.gradewise.ui.screens

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vitiligo.gradewise.ui.components.CGPACard
import com.vitiligo.gradewise.ui.components.SemesterList
import com.vitiligo.gradewise.ui.theme.GradeWiseTheme
import com.vitiligo.gradewise.ui.utils.ScrollDirection
import com.vitiligo.gradewise.ui.utils.rememberDirectionalLazyListState
import com.vitiligo.gradewise.ui.viewmodels.HomeUiState
import com.vitiligo.gradewise.ui.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToSemester: (String, String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyListState = rememberLazyListState()
    val directionalLazyListState = rememberDirectionalLazyListState(
        lazyListState
    )

    val isVisible by remember {
        derivedStateOf {
            directionalLazyListState.scrollDirection == ScrollDirection.None || directionalLazyListState.scrollDirection == ScrollDirection.Up
        }
    }

    val uiState: HomeUiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ScreenAppBar(
                title = "GradeWise",
                onSettingsButtonClick = { },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 })
            ) {
                FloatingActionButton(
                    onClick = viewModel::addSemester,
                ) {
                    Icon(Icons.Filled.Add, null)
                }
            }
        }
    ) {
        HomeScreenBody(
            uiState = uiState,
            lazyListState = lazyListState,
            onSemesterClick = navigateToSemester,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        )
    }
}

@Composable
fun HomeScreenBody(
    uiState: HomeUiState,
    lazyListState: LazyListState,
    onSemesterClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d("HomeScreen", "Semesters Count: ${uiState.semesters.size}, list: ${uiState.semesters.toString()}")
    LazyColumn(
        state = lazyListState,
        modifier = modifier
    ) {
        item {
            CGPACard(
                cgpa = uiState.cgpa,
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxSize()
            )
        }
        item {
            SemesterList(
                semesters = uiState.semesters,
                onSemesterClick = onSemesterClick,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenAppBar(
    title: String,
    onSettingsButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        actions = {
            IconButton(
                onClick = onSettingsButtonClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL_7_PRO,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenPreview() {
    GradeWiseTheme {
        HomeScreen(
            navigateToSemester = { _id, _name -> }
        )
    }
}