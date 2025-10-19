package com.fatherofapps.fc671

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource

import fc671.composeapp.generated.resources.Res
import fc671.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.Flow

@Preview
@Composable
fun App(
    viewModel: AppViewModel = viewModel { AppViewModel() },
) {
    val navController = rememberNavController()
    MaterialTheme {

        Box(
            modifier = Modifier
                .fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer).padding(
                    vertical = 24.dp,
                    horizontal = 16.dp
                ), // Full height & width
            contentAlignment = Alignment.TopCenter // Center horizontally for large screens
        ) {
            NavHost(navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(viewModel = viewModel, onCreateMatch = { navController.navigate("create_match")},
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

                composable(route = "match_histories"){
                    MatchHistories(viewModel = viewModel, onBack = { navController.popBackStack() })
                }

            }
        }
    }
}
