package com.fatherofapps.fc671

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.onClick
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.fatherofapps.fc671.components.FBox
import com.fatherofapps.fc671.components.FScaffold
import com.fatherofapps.fc671.components.FTopBar
import com.fatherofapps.fc671.components.FUnderlineButton
import kotlin.random.Random

@Composable
fun CreateMatch(
    viewModel: AppViewModel,
    onBack: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()

    var availablePlayers by remember {
        mutableStateOf<List<String>>(
            allPlayers
        )
    }

    var selectedPlayers by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    var selectedCaptains by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    var isSelectedVotePlayers by remember { mutableStateOf(false) }

    val isLoading by remember(uiState.isLoading) { mutableStateOf(uiState.isLoading) }


    LaunchedEffect(uiState.isCreatedMatch) {
        if (uiState.isCreatedMatch == true) {
            onBack()
        }
    }

    FScaffold(topBar = {
        FTopBar(modifier = Modifier.fillMaxWidth(), title = "Tạo trận đấu") {
            onBack()
        }
    }) {

        Text("Bước 1: Chọn cầu thủ đã vote ra sân hôm nay", style = MaterialTheme.typography.titleMedium)
        FBox(modifier = Modifier.fillMaxWidth()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availablePlayers.forEach { player ->
                    SuggestionChip(onClick = {
                        val players = availablePlayers.toMutableList()
                        players.remove(player)
                        availablePlayers = players

                        val currentSelectedPlayers = selectedPlayers.toMutableList()
                        currentSelectedPlayers.add(player)
                        selectedPlayers = currentSelectedPlayers

                    }, label = { Text(player) })
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Cầu thủ đã chọn:", style = MaterialTheme.typography.titleMedium)
            if (isSelectedVotePlayers) {

                FUnderlineButton(modifier = Modifier.wrapContentSize(), title = "Chọn lại cầu thủ") {
                    isSelectedVotePlayers = !isSelectedVotePlayers
                }

            }
        }
        FBox(modifier = Modifier.fillMaxWidth()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedPlayers.forEach { player ->
                    SuggestionChip(onClick = {
                        if (isSelectedVotePlayers) {
                            // selecting captain
                            val currentSelectedCaptains = selectedCaptains.toMutableList()
                            currentSelectedCaptains.add(player)
                            selectedCaptains = currentSelectedCaptains

                        } else {
                            val players = availablePlayers.toMutableList()
                            players.add(player)
                            availablePlayers = players
                        }

                        val currentSelectedPlayers = selectedPlayers.toMutableList()
                        currentSelectedPlayers.remove(player)
                        selectedPlayers = currentSelectedPlayers
                    }, label = { Text(player) })
                }
            }

        }

        if (isSelectedVotePlayers) {
            Text("Bước 2: Chọn Captain từ danh sách cầu đủ đã vote", style = MaterialTheme.typography.titleMedium)
            FBox(modifier = Modifier.fillMaxWidth()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedCaptains.forEach { player ->
                        SuggestionChip(onClick = {}, label = { Text(player) })
                    }
                }
            }
        } else if (selectedPlayers.isNotEmpty()) {
            Button(onClick = {
                isSelectedVotePlayers = true
            }) {
                Text("Xác nhận chọn xong cầu thủ")
            }

        }

        if (isSelectedVotePlayers && selectedCaptains.size > 1) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(onClick = {
                    if (!isLoading) {
                        // tao tr
                        val teams = selectedCaptains.mapIndexed { index, captain ->
                            TeamData(
                                id = index + 1,
                                leaderName = captain,
                            )
                        }
                        val selectingTeamId = teams.map {
                            it.id
                        }.random()
                        val match = MatchData(
                            selectedPlayers = selectedPlayers,
                            teams = teams,
                            onGoing = true,
                            selectingTeamId = selectingTeamId,
                        )



                        viewModel.createMatch(match)
                    }
                }) {
                    Text("Tạo trận đấu")
                }
            }
        }
    }


}
