package com.example.codewizard

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.codewizard.navigation.ProjectNavHost

@Composable
fun CodeWizardApp(navHostController: NavHostController = rememberNavController()) {
    ProjectNavHost(navController = navHostController)
}