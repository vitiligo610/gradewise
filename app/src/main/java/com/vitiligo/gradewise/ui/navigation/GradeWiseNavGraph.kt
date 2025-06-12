package com.vitiligo.gradewise.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.vitiligo.gradewise.ui.screens.HomeScreen
import com.vitiligo.gradewise.ui.screens.SemesterScreen
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Semester(
    val id: Int,
    val name: String
)

@Composable
fun GradeWiseNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
        modifier = modifier
    ) {
        composable<Home> {
            HomeScreen(
                navigateToSemester = { semesterId, semesterName -> navController.navigate(Semester(semesterId, semesterName)) }
            )
        }

        composable<Semester> {
            val semester: Semester = it.toRoute()
            SemesterScreen(
                semesterId = semester.id,
                semesterName = semester.name,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
