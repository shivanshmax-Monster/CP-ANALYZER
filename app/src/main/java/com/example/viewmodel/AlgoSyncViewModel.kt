package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.model.Difficulty
import com.example.model.ProblemItem
import com.example.model.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AppTab(val label: String) {
    DASHBOARD("Dashboard"),
    REVISIT("Revisit"),
    VAULT("Vault"),
    ANALYTICS("Analytics"),
    SYNC("Sync")
}

data class AlgoUiState(
    val currentTab: AppTab = AppTab.DASHBOARD,
    val streakDays: Int = 42,
    val totalSolved: Int = 648,
    val easySolved: Int = 284,
    val medSolved: Int = 292,
    val hardSolved: Int = 72,
    val retentionPercent: Double = 98.4,
    val todayTargetCompleted: Int = 2,
    val todayTargetTotal: Int = 3,
    val problems: List<ProblemItem> = SampleData.initialProblems,
    val vaultPlatformFilter: String = "All",
    val vaultStatusFilter: String? = null,
    val vaultSearchQuery: String = "",
    val revisitFilter: String = "Due Today",
    val isBlindRetryMode: Boolean = true,
    val showLogDialog: Boolean = false,
    val activeBlindRetryProblem: ProblemItem? = null,
    val peekHintActive: Boolean = false
)

class AlgoSyncViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AlgoUiState())
    val uiState: StateFlow<AlgoUiState> = _uiState.asStateFlow()

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setVaultPlatformFilter(platform: String) {
        _uiState.update { it.copy(vaultPlatformFilter = platform) }
    }

    fun setVaultStatusFilter(status: String?) {
        _uiState.update { 
            val newStatus = if (it.vaultStatusFilter == status) null else status
            it.copy(vaultStatusFilter = newStatus) 
        }
    }

    fun setVaultSearchQuery(query: String) {
        _uiState.update { it.copy(vaultSearchQuery = query) }
    }

    fun setRevisitFilter(filter: String) {
        _uiState.update { it.copy(revisitFilter = filter) }
    }

    fun setBlindRetryMode(enabled: Boolean) {
        _uiState.update { it.copy(isBlindRetryMode = enabled) }
    }

    fun showLogDialog(show: Boolean) {
        _uiState.update { it.copy(showLogDialog = show) }
    }

    fun startBlindRetry(problem: ProblemItem) {
        _uiState.update { 
            it.copy(
                activeBlindRetryProblem = problem,
                peekHintActive = false
            ) 
        }
    }

    fun dismissBlindRetry() {
        _uiState.update { it.copy(activeBlindRetryProblem = null) }
    }

    fun togglePeekHint() {
        _uiState.update { it.copy(peekHintActive = !it.peekHintActive) }
    }

    fun logSpacedRepetitionResult(problemId: String, grade: String) {
        _uiState.update { state ->
            val updated = state.problems.map { p ->
                if (p.id == problemId) {
                    when (grade) {
                        "Fail" -> p.copy(
                            stageText = "Stage 1 (1d cadence)",
                            dueStatus = "Due in 1d",
                            isDueToday = false,
                            masteryPercent = maxOf(20, p.masteryPercent - 15),
                            consecutiveRecalls = 0
                        )
                        "Hard" -> p.copy(
                            stageText = "Stage 1 (3d cadence)",
                            dueStatus = "Due in 3d",
                            isDueToday = false,
                            masteryPercent = minOf(100, p.masteryPercent + 5)
                        )
                        "Good" -> p.copy(
                            stageText = "Stage 2 (7d cadence)",
                            dueStatus = "Due in 7d",
                            isDueToday = false,
                            masteryPercent = minOf(100, p.masteryPercent + 15),
                            consecutiveRecalls = p.consecutiveRecalls + 1
                        )
                        "Easy" -> p.copy(
                            stageText = "Stage 3 (14d cadence)",
                            dueStatus = "Due in 14d",
                            isDueToday = false,
                            masteryPercent = minOf(100, p.masteryPercent + 25),
                            consecutiveRecalls = p.consecutiveRecalls + 1
                        )
                        else -> p
                    }
                } else p
            }
            val newCompleted = minOf(state.todayTargetTotal, state.todayTargetCompleted + 1)
            state.copy(
                problems = updated, 
                todayTargetCompleted = newCompleted,
                activeBlindRetryProblem = null
            )
        }
    }

    fun markMastered(problemId: String) {
        _uiState.update { state ->
            val updated = state.problems.map { p ->
                if (p.id == problemId) {
                    p.copy(
                        isMastered = true,
                        isDueToday = false,
                        stageText = "Mastered (45d+)",
                        dueStatus = "Mastered",
                        masteryPercent = 100,
                        consecutiveRecalls = p.consecutiveRecalls + 1
                    )
                } else p
            }
            state.copy(problems = updated, activeBlindRetryProblem = null)
        }
    }

    fun snoozeProblem(problemId: String, days: Int) {
        _uiState.update { state ->
            val updated = state.problems.map { p ->
                if (p.id == problemId) {
                    p.copy(
                        dueStatus = "Snoozed +${days}d",
                        isDueToday = false
                    )
                } else p
            }
            state.copy(problems = updated)
        }
    }

    fun addNewProblem(
        title: String,
        number: String,
        platform: String,
        difficulty: Difficulty,
        tags: List<String>,
        timeComp: String,
        spaceComp: String,
        notes: String
    ) {
        val newProblem = ProblemItem(
            id = "custom-${System.currentTimeMillis()}",
            number = number.ifBlank { "Custom" },
            title = title,
            platform = platform,
            difficulty = difficulty,
            tags = tags.ifEmpty { listOf("Algorithms") },
            complexityTime = timeComp.ifBlank { "O(N)" },
            complexitySpace = spaceComp.ifBlank { "O(1)" },
            stageText = "Stage 1 (3d)",
            dueStatus = "Stage 1 (3d)",
            isDueToday = false,
            masteryPercent = 50,
            consecutiveRecalls = 1,
            intuitionNote = notes,
            timeAgo = "Just now",
            intervalDays = 3
        )
        _uiState.update { state ->
            val newTotal = state.totalSolved + 1
            val newEasy = if (difficulty == Difficulty.EASY) state.easySolved + 1 else state.easySolved
            val newMed = if (difficulty == Difficulty.MEDIUM) state.medSolved + 1 else state.medSolved
            val newHard = if (difficulty == Difficulty.HARD) state.hardSolved + 1 else state.hardSolved
            state.copy(
                problems = listOf(newProblem) + state.problems,
                totalSolved = newTotal,
                easySolved = newEasy,
                medSolved = newMed,
                hardSolved = newHard,
                showLogDialog = false
            )
        }
    }
}
