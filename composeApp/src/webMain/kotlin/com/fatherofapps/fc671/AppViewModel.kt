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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class MatchData(
    @SerialName("selected_players")
    val selectedPlayers: List<String> = listOf(),
    val name: String = "",
    @SerialName("selecting_team_id")
    val selectingTeamId: Int = 0,
    val teams: List<TeamData> = emptyList(),
    @SerialName("on_going")
    val onGoing: Boolean = false,
    @Transient
    val id: String = ""
) {
    fun validSelectedPlayer() = selectedPlayers.filter { it.isNotEmpty() && it != "null" }
}

@Serializable
data class TeamData(
    @SerialName("team_id")
    val id: Int = 0,
    @SerialName("leader_name")
    val leaderName: String = "",
    val players: List<String> = listOf(),
) {
    fun validPlayers() = players.filter { it.isNotEmpty() && it != "null" }
}


data class AppState(
    val matches: List<MatchData> = listOf(),
    val isLoading: Boolean = false,
    val isCreatedMatch: Boolean? = null,
    val isHosted: Boolean = false,
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
                    runCatching {
                        val match = doc.data<MatchData>(MatchData.serializer())
                        match.copy(id = doc.id)
                    }.getOrNull()
                }
                _uiState.update { it.copy(matches = matches) }
            }
        }
    }

    fun completeMatch(match: MatchData) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            updateMatch(match.copy(onGoing = false))
        }
    }

    fun selectPlayer(player: String, match: MatchData) {
        if(match.id.trim().isEmpty()) return
        console.log("Selecting player $player")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val selectedPlayers = match.selectedPlayers.toMutableList()
            selectedPlayers.remove(player)
            console.log("Selected player $selectedPlayers")
            val updatedTeams = match.teams.map { team ->
                if (team.id == match.selectingTeamId) {
                    val playersOfSelectingTeam = team.players.toMutableList()
                    playersOfSelectingTeam.add(player)
                    team.copy(players = playersOfSelectingTeam)
                } else {
                    team
                }
            }

            var nextSelectingTeamId = (match.selectingTeamId + 1)
            if (nextSelectingTeamId > match.teams.size) nextSelectingTeamId = 1

            console.log("Selecting team $nextSelectingTeamId")
            console.log("Updated team $updatedTeams")
            val updatedMatch = match.copy(
                selectedPlayers = selectedPlayers,
                teams = updatedTeams,
                selectingTeamId = nextSelectingTeamId
            )
            console.log("Updated Match: $updatedMatch")
            updateMatch(updatedMatch)
        }
    }

    fun createMatch(match: MatchData) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isCreatedMatch = null) }
            updateMatch(match.copy(name = generateMatchName()))
        }
    }

    private fun generateMatchName(): String {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return "Trận ${now.dayOfMonth}-${now.month}-${now.year}"
    }

    private suspend fun updateMatch(match: MatchData) {
        try {
            val documentID = match.id.takeIf { it.trim().isNotEmpty() } ?: db.collection("matches").document.id
            val docRef =
                db.collection("matches").document(documentID)

            docRef.set(match)
            console.log("✅ Match created: ${match.name}")
            _uiState.update { it.copy(isLoading = false, isCreatedMatch = true, isHosted = true) }
        } catch (e: Exception) {
            console.error("❌ Failed to create match:", e)
            _uiState.update { it.copy(isLoading = false, isCreatedMatch = false) }
        }
    }


}