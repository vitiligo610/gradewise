package com.vitiligo.gradewise

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.vitiligo.gradewise.ui.navigation.GradeWiseNavHost

@Composable
fun GradeWiseApp() {
    GradeWiseNavHost(
        navController = rememberNavController()
    )
}