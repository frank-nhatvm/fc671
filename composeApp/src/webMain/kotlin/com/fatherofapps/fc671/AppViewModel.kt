package com.fatherofapps.fc671

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchData(
    @SerialName("selected_players")
    val selectedPlayers: List<String> = listOf(),
    val name: String = "",
    @SerialName("selecting_team_id")
    val selectingTeamId: Int = 0,
    val teams: List<TeamData> = emptyList(),
    @SerialName("on_going")
    val onGoing:Boolean = true,
)

@Serializable
data class TeamData(
    @SerialName("team_id")
    val id: Int = 0,
    @SerialName("leader_name")
    val leaderName: String = "",
    val players: List<String> = listOf(),
)


data class AppState(
    val matches: List<MatchData> = listOf(),
)

class AppViewModel : ViewModel() {

    val db = Firebase.firestore

    private val _uiState = MutableStateFlow<AppState>(AppState())
    val uiState: StateFlow<AppState> = _uiState.asStateFlow()


    init {
        viewModelScope.launch {

            db.collection("matches").snapshots.collect { querySnapshot ->
                console.log("Snapshot received: ${querySnapshot.documents.size}")
                val matches = querySnapshot.documents.mapNotNull { doc ->
                    runCatching { doc.data<MatchData>(MatchData.serializer()) }.getOrNull()
                }
                _uiState.update { it.copy(matches = matches) }
            }
        }


    }


}