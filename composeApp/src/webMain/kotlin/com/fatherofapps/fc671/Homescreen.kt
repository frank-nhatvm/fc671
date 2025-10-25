package com.fatherofapps.fc671

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.fatherofapps.fc671.components.CopyToClipboardButton
import com.fatherofapps.fc671.components.FBox
import com.fatherofapps.fc671.components.FScaffold
import com.fatherofapps.fc671.components.FTopBar
import com.fatherofapps.fc671.components.FUnderlineButton
import org.jetbrains.compose.resources.Resource
import kotlin.text.matches


@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onCreateMatch: () -> Unit,
    onViewHistories: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    var isSignedInAsHost by remember(uiState) { mutableStateOf(uiState.isHosted) }

    val latestMatch by remember(uiState.matches) {
        derivedStateOf { uiState.matches.firstOrNull { !it.onGoing } }
    }

    val onGoingMatch by remember(uiState) {
        derivedStateOf {
            uiState.matches.firstOrNull { it.onGoing }
        }
    }
    val isLoading by remember(uiState.isLoading) { mutableStateOf(uiState.isLoading) }

    val isCreatedAMatch by remember(uiState.isCreatedMatch) {
        derivedStateOf {
            uiState.isCreatedMatch ?: false
        }
    }

    val isShowHistory by remember(uiState.matches) {
        derivedStateOf { uiState.matches.any { !it.onGoing } }
    }

    FScaffold(
        topBar = { FTopBar(modifier = Modifier.fillMaxWidth(), title = "FC671") }
    ) {

        if (!isCreatedAMatch && onGoingMatch == null) {
            Button(onClick = {
                onCreateMatch()
            }) {
                Text("Tạo trận đấu")
            }
        }


        if (onGoingMatch != null) {
            SelectingTeam(
                viewModel = viewModel,
                match = onGoingMatch!!,
                isHosted = isSignedInAsHost,
                isLoading = isLoading
            )
        } else if (latestMatch != null) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    "Trận gần nhất: ${latestMatch!!.name} ", style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.padding(top = 24.dp)
                )

                CopyToClipboardButton(modifier = Modifier.size(24.dp), textToCopy = latestMatch!!.textToCopy())

            }

            FBox(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                TeamCards(modifier = Modifier.fillMaxWidth(), teams = latestMatch!!.teams)
            }
        }


        if (isShowHistory) {
            FUnderlineButton(
                modifier = Modifier.wrapContentSize().padding(top = 12.dp),
                title = "Lịch sử các trận đấu "
            ) {
                onViewHistories()
            }
        }


    }

}

@Composable
fun SelectingTeam(viewModel: AppViewModel, match: MatchData, isHosted: Boolean, isLoading: Boolean) {

    val hasAvailablePlayer by remember(match) {
        derivedStateOf {
            match.validSelectedPlayer().isNotEmpty()
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
        Row {
            Text(match.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Spacer(Modifier.width(8.dp))
            if (isLoading) {
                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Cầu thủ có thể chọn:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

        if (hasAvailablePlayer) {
            LoadingBox(Modifier.fillMaxWidth(), isLoading = isLoading && isHosted) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    match.validSelectedPlayer().forEach { player ->

                        SuggestionChip(
                            onClick = {
                                if (isHosted) {
                                    viewModel.selectPlayer(player = player, match = match)
                                }
                            },
                            label = {
                                Text(player)
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Teams", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        LoadingBox(Modifier.fillMaxWidth(), isLoading = isLoading && isHosted) {
            Column {
                TeamCards(
                    modifier = Modifier.fillMaxWidth(),
                    teams = match.teams,
                    selectingTeamID = match.selectingTeamId,
                    previousSelectedTeamId = match.previousSelectedTeamId,
                ) { player, teamId ->
                    viewModel.unSelectPlayer(player, teamId, match)
                }

                if (!hasAvailablePlayer) {
                    Button(modifier = Modifier.padding(top = 24.dp), onClick = {
                        viewModel.completeMatch(match)
                    }) {
                        Text("Hoàn thành")
                    }
                }
            }
        }

    }
}

@Composable
fun LoadingBox(modifier: Modifier = Modifier, isLoading: Boolean, content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        content()
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().clickable(onClick = {

            }), contentAlignment = Alignment.Center) {

            }
        }
    }
}
