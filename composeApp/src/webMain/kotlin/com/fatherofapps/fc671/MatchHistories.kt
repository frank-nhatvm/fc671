package com.fatherofapps.fc671

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MatchHistories(viewModel: AppViewModel, onBack:()->Unit) {
    val uiState by viewModel.uiState.collectAsState()

    val matches by remember(uiState) { derivedStateOf { uiState.matches.filter { !it.onGoing }} }

    Button(onClick = onBack) {
        Text("Back")
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(matches) { match ->
            Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                Text(match.name, style = MaterialTheme.typography.titleLarge)

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Captain",
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        "Cầu thủ",
                        modifier = Modifier.weight(4F),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                match.teams.map { team ->
                    Row {
                        Text(team.leaderName, modifier = Modifier.weight(1F))
                        FlowRow(
                            modifier = Modifier.weight(4F).padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (team.validPlayers().isNotEmpty()) {
                                team.validPlayers().forEach { player ->
                                    SuggestionChip(
                                        onClick = {},
                                        label = {
                                            Text(player)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}