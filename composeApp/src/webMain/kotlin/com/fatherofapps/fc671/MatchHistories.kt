package com.fatherofapps.fc671

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fatherofapps.fc671.components.CopyToClipboardButton
import com.fatherofapps.fc671.components.FBox
import com.fatherofapps.fc671.components.FScaffold
import com.fatherofapps.fc671.components.FTopBar
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchHistories(viewModel: AppViewModel, onBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    val matches by remember(uiState) { derivedStateOf { uiState.matches.filter { !it.onGoing } } }

    FScaffold(isScrollable = false, topBar = {
        FTopBar(modifier = Modifier.fillMaxWidth(), title = "Các trận đấu đã diễn ra") {
            onBack()
        }
    }) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 12.dp)) {
            items(matches) { match ->
                Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(match.name, style = MaterialTheme.typography.titleLarge)
                        CopyToClipboardButton(modifier = Modifier.size(24.dp), textToCopy = match.textToCopy())
                    }
                    FBox(modifier = Modifier.fillMaxWidth()) {
                        TeamCards(modifier = Modifier.fillMaxWidth(), teams = match.teams)
                    }
                }
            }
        }
    }
}

@Composable
fun TeamCards(
    modifier: Modifier = Modifier,
    teams: List<TeamData>,
    selectingTeamID: Int? = null,
    previousSelectedTeamId: Int? = null,
    onSelectPlayer: ((String, Int) -> Unit)? = null
) {
    var dotCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            dotCount = (dotCount + 1) % 4   // 0,1,2,3 → repeat
            delay(500)                      // change every 0.5s
        }
    }

    val dots by remember(dotCount) {
        derivedStateOf {
            ".".repeat(dotCount)
        }
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Captain",
                modifier = Modifier.weight(2F),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            )
            Text(
                "Cầu thủ",
                modifier = Modifier.weight(3F),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
        HorizontalDivider()
        teams.map { team ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(2F).padding(vertical = 2.dp)) {
                    Text(text = "${team.leaderName} - ${team.id} ")
                    val updateText = if (selectingTeamID == team.id) {
                        "Đang chọn $dots"

                    } else {
                        " "
                    }
                    Text(updateText, modifier = Modifier.padding(top = 2.dp), color = Color.Red)
                }
                FlowRow(
                    modifier = Modifier.weight(3F).padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (team.validPlayers().isNotEmpty()) {
                        team.validPlayers().forEach { player ->
                            SuggestionChip(
                                onClick = {
                                    onSelectPlayer?.invoke(player, team.id)
                                },
                                label = {
                                    Text(player)
                                },
                                enabled = team.id == previousSelectedTeamId,
                            )
                        }
                    }
                }
            }
            HorizontalDivider()
        }
    }

}
