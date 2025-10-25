package com.fatherofapps.fc671

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun App(
    viewModel: AppViewModel = viewModel { AppViewModel() },
) {
    val navController = rememberNavController()
    MaterialTheme {

        Box(
            modifier = Modifier
                .fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer), // Full height & width
            contentAlignment = Alignment.TopCenter // Center horizontally for large screens
        ) {
                NavHost(navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel, onCreateMatch = { navController.navigate("create_match") },
                            onViewHistories = {
                                navController.navigate("match_histories")
                            }
                        )
                    }

                    composable(route = "create_match") {
                        CreateMatch(viewModel = viewModel) {
                            navController.popBackStack()
                        }
                    }

                    composable(route = "match_histories") {
                        MatchHistories(viewModel = viewModel, onBack = { navController.popBackStack() })
                    }
                }

        }
    }
}
