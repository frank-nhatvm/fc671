package com.fatherofapps.fc671

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fatherofapps.fc671.components.FBox
import com.fatherofapps.fc671.components.FScaffold
import com.fatherofapps.fc671.components.FTopBar
import fc671.composeapp.generated.resources.Res
import fc671.composeapp.generated.resources.arrow_back
import fc671.composeapp.generated.resources.ic_back
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchHistories(viewModel: AppViewModel, onBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    val matches by remember(uiState) { derivedStateOf { uiState.matches.filter { !it.onGoing } } }

    FScaffold(isScrollable = false, topBar = {   FTopBar(modifier = Modifier.fillMaxWidth(), title = "Các trận đấu đã diễn ra") {
        onBack()
    }}) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 12.dp)) {
            items(matches) { match ->
                Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                    Text(match.name, style = MaterialTheme.typography.titleLarge)
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
    onSelectPlayer: ((String) -> Unit)? = null
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
                Column( modifier = Modifier.weight(2F).padding(vertical = 2.dp)) {
                    Text(text = "${team.leaderName} - ${team.id} ", )
                  val updateText = if (selectingTeamID == team.id) {
                        "Đang chọn $dots"

                    }else{
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
                                    onSelectPlayer?.invoke(player)
                                },
                                label = {
                                    Text(player)
                                }
                            )
                        }
                    }
                }
            }
            HorizontalDivider()
        }
    }

}
