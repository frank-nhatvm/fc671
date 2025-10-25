package com.fatherofapps.fc671

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
    FScaffold(
        topBar = { FTopBar(modifier = Modifier.fillMaxWidth(), title = "FC671") }
    ) {

        if (!isCreatedAMatch  && onGoingMatch == null) {
            Button(onClick = {
                onCreateMatch()
            }) {
                Text("Tạo trận đấu")
            }
        }


        if (onGoingMatch != null) {
            SelectingTeam(viewModel = viewModel, match = onGoingMatch!!, isHosted = isSignedInAsHost)
        } else if (latestMatch != null) {
            Text(
                "Trận gần nhất: ${latestMatch!!.name} ", style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.padding(top = 24.dp)
            )

            FBox(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                TeamCards(modifier = Modifier.fillMaxWidth(), teams = latestMatch!!.teams)
            }
        }

        FUnderlineButton(modifier = Modifier.wrapContentSize().padding(top = 12.dp), title = "Lịch sử các trận đấu ") {
            onViewHistories()
        }


        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

    }

}

@Composable
fun SelectingTeam(viewModel: AppViewModel, match: MatchData, isHosted: Boolean) {

    val hasAvailablePlayer by remember(match) {
        derivedStateOf {
            match.validSelectedPlayer().isNotEmpty()
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
        Text(match.name, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Cầu thủ có thể chọn:", style = MaterialTheme.typography.titleMedium)

        if (hasAvailablePlayer) {
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
        Spacer(modifier = Modifier.height(8.dp))
        Text("Teams", style = MaterialTheme.typography.titleMedium)

        TeamCards(
            modifier = Modifier.fillMaxWidth(),
            teams = match.teams,
            selectingTeamID = match.selectingTeamId,
        ) { player ->

        }

        if(!hasAvailablePlayer) {
            Button(onClick = {
                viewModel.completeMatch(match)
            }){
                Text("Hoàn thành")
            }
        }

    }
}
