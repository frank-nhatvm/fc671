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
import kotlinx.coroutines.flow.Flow

@Preview
@Composable
fun App(
    viewModel: AppViewModel = viewModel { AppViewModel() },
) {
    val uiState by viewModel.uiState.collectAsState()

    var isSignedInAsHost by remember { mutableStateOf(false) }

    var isSigningInAsHost by remember { mutableStateOf(false) }

    var hostName by remember { mutableStateOf("") }
    var hostPassword by remember { mutableStateOf("") }

    val aMatchOnGoing by remember(uiState) {
        derivedStateOf {
            uiState.matches.any { it.onGoing }
        }
    }


    MaterialTheme {


        Box(
            modifier = Modifier
                .fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer).padding(
                    vertical = 24.dp,
                    horizontal = 16.dp
                ), // Full height & width
            contentAlignment = Alignment.TopCenter // Center horizontally for large screens
        ) {
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
                        val textButton =
                            if (isSigningInAsHost) {
                                "Huỷ"
                            } else if (isSignedInAsHost) {
                                "Đăng xuất"
                            } else {
                                "Đăng nhập để trở thành chủ xị"
                            }
                        Button(onClick = {
                            if (isSigningInAsHost) {
                                // huy dang nhap
                                isSigningInAsHost = false
                            } else if (isSigningInAsHost) {
                                // dang xuat
                                isSigningInAsHost = false
                            } else {
                                isSigningInAsHost = true
                            }
                        }) {
                            Text(textButton)
                        }
                    }

                    if (isSigningInAsHost) {
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
                            Button(
                                onClick = {
                                    if (hostName == "admin" && hostPassword == "fc671admin@2025") {
                                        isSigningInAsHost = false
                                        isSignedInAsHost = true
                                        hostName = ""
                                        hostPassword = ""
                                    }
                                },
                                enabled = hostName.isNotEmpty() && hostPassword.isNotEmpty(),
                            ) {
                                Text("Dang nhap")
                            }
                        }
                    }

                }


                if (isSignedInAsHost && !aMatchOnGoing) {
                    Button(onClick = {}) {
                        Text("Tạo trận đấu")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn() {
                    items(items = uiState.matches) { match ->
                        Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                            Text(match.name, style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Người chơi có thể chọn:", style = MaterialTheme.typography.titleMedium)

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                match.selectedPlayers.forEach { player ->

                                    SuggestionChip(
                                        onClick = {},
                                        label = {
                                            Text(player)
                                        }
                                    )
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
                                    "Captain",
                                    modifier = Modifier.weight(4F),
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                            match.teams.map { team ->
                                Row {
                                    val builder = StringBuilder()
                                    builder.append(team.leaderName)
                                    if (match.selectingTeamId == team.id) {
                                        builder.append("<<< Đang chọn")
                                    }
                                    Text(builder.toString(), modifier = Modifier.weight(1F))
                                    FlowRow(
                                        modifier = Modifier.weight(4F).padding(8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        team.players.forEach { player ->

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
    }
}
