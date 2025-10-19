package com.fatherofapps.fc671

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlin.text.matches


@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onCreateMatch: () -> Unit,
    onViewHistories: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    var isSignedInAsHost by remember(uiState) { mutableStateOf(uiState.isHosted) }

    var isSigningInAsHost by remember { mutableStateOf(false) }

    var hostName by remember { mutableStateOf("") }
    var hostPassword by remember { mutableStateOf("") }

    val aMatchOnGoing by remember(uiState) {
        derivedStateOf {
            uiState.matches.any { it.onGoing }
        }
    }

    val onGoingMatch by remember(uiState) {
        derivedStateOf {
            uiState.matches.firstOrNull { it.onGoing }
        }
    }
    val isLoading by remember(uiState.isLoading) { mutableStateOf(uiState.isLoading) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize().widthIn(max = 420.dp)
                .safeContentPadding(),
            horizontalAlignment = Alignment.Start,
        ) {


            Column {
                Text("Chủ xị có quyền tạo danh sách người chơi, chọn đội trưởng và điều phối việc chọn team.")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    if (isSignedInAsHost) {
                        Text("Bạn đang là chủ xị")
                    }
                    if (!isSigningInAsHost) {
                        val textButton =
                            if (isSignedInAsHost) {
                                "Đăng xuất"
                            } else {
                                "Đăng nhập để trở thành chủ xị"
                            }
                        Button(onClick = {

                            if (isSignedInAsHost) {
                                viewModel.signOut()
                            } else {
                                isSigningInAsHost = true
                            }


                        }) {
                            Text(textButton)
                        }
                    }
                }

                if (isSigningInAsHost ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                        OutlinedTextField(
                            value = hostName,
                            onValueChange = { hostName = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Hostname") },
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = hostPassword,
                            onValueChange = { hostPassword = it },
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                            label = { Text("Password") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = {
                                isSigningInAsHost = false
                            }) {
                                Text("Huy")
                            }
                            Button(
                                onClick = {
                                    viewModel.signIn(hostName, hostPassword)
                                    isSigningInAsHost = false
                                },
                                enabled = hostName.isNotEmpty() && hostPassword.isNotEmpty(),
                            ) {
                                Text("Dang nhap")
                            }
                        }

                    }
                }

            }


            if (isSignedInAsHost && !aMatchOnGoing) {
                Button(onClick = {
                    onCreateMatch()
                }) {
                    Text("Tạo trận đấu")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (onGoingMatch != null) {
                SelectingTeam(viewModel = viewModel, match = onGoingMatch!!, isHosted = isSignedInAsHost)
            } else if (!isSignedInAsHost) {
                Text("Vui lòng đợi chủ xị tạo trận đấu")
            }

            OutlinedButton(onClick = {
                onViewHistories()
            }) {
                Text(
                    "Lịch sử các trận đấu",
                    style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.Underline)
                )
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
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

        if(hasAvailablePlayer) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                match.validSelectedPlayer().forEach { player ->

                    SuggestionChip(
                        onClick = {
                            if(isHosted) {
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
                val builder = StringBuilder()
                builder.append("${team.leaderName} - ${team.id} ")

                if (match.selectingTeamId == team.id) {
                    builder.append("<<< Đang chọn")
                }
                Text(builder.toString(), modifier = Modifier.weight(1F))
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
