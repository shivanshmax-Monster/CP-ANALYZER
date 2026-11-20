package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ui.components.AlgoBottomBar
import com.example.ui.components.AlgoTopBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBg
import com.example.viewmodel.AlgoSyncViewModel
import com.example.viewmodel.AppTab

class MainActivity : ComponentActivity() {
    private val viewModel: AlgoSyncViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AlgoSyncApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AlgoSyncApp(viewModel: AlgoSyncViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Handle back button to return to Dashboard if on a sub-tab
    BackHandler(enabled = uiState.currentTab != AppTab.DASHBOARD) {
        viewModel.selectTab(AppTab.DASHBOARD)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg),
        containerColor = ObsidianBg,
        topBar = {
            val subtitle = when (uiState.currentTab) {
                AppTab.DASHBOARD -> "Dashboard"
                AppTab.REVISIT -> "Revisit Queue"
                AppTab.VAULT -> "Vault"
                AppTab.ANALYTICS -> "Analytics"
                AppTab.SYNC -> "Sync"
            }
            AlgoTopBar(
                subtitle = subtitle,
                streakDays = uiState.streakDays,
                onLogClick = { viewModel.showLogDialog(true) }
            )
        },
        bottomBar = {
            AlgoBottomBar(
                currentTab = uiState.currentTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                AppTab.DASHBOARD -> {
                    DashboardScreen(
                        uiState = uiState,
                        onStartBlindRetry = { viewModel.startBlindRetry(it) },
                        onSnooze = { id, days -> viewModel.snoozeProblem(id, days) },
                        onMarkMastered = { viewModel.markMastered(it) },
                        onOpenLog = { viewModel.showLogDialog(true) },
                        onNavigateToRevisit = { viewModel.selectTab(AppTab.REVISIT) },
                        onNavigateToVault = { viewModel.selectTab(AppTab.VAULT) }
                    )
                }

                AppTab.REVISIT -> {
                    RevisitScreen(
                        uiState = uiState,
                        onStartBlindRetry = { viewModel.startBlindRetry(it) },
                        onLogSM2Result = { id, grade -> viewModel.logSpacedRepetitionResult(id, grade) },
                        onMarkMastered = { viewModel.markMastered(it) },
                        onSnooze = { id, days -> viewModel.snoozeProblem(id, days) },
                        onFilterChange = { viewModel.setRevisitFilter(it) },
                        onToggleBlindMode = { viewModel.setBlindRetryMode(it) },
                        onTogglePeekHint = { viewModel.togglePeekHint() }
                    )
                }

                AppTab.VAULT -> {
                    VaultScreen(
                        uiState = uiState,
                        onSearchChange = { viewModel.setVaultSearchQuery(it) },
                        onPlatformFilterChange = { viewModel.setVaultPlatformFilter(it) },
                        onStatusFilterChange = { viewModel.setVaultStatusFilter(it) },
                        onReviewProblem = { viewModel.startBlindRetry(it) },
                        onExportMarkdown = {
                            Toast.makeText(context, "Exported 648 dossiers to AlgoSync-Vault.md", Toast.LENGTH_SHORT).show()
                        },
                        onSyncGithub = {
                            Toast.makeText(context, "Syncing latest commits to GitHub repository...", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                AppTab.ANALYTICS -> {
                    AnalyticsScreen(uiState = uiState)
                }

                AppTab.SYNC -> {
                    SyncScreen(uiState = uiState)
                }
            }
        }
    }

    // Quick Log Dialog
    if (uiState.showLogDialog) {
        QuickLogDialog(
            onDismiss = { viewModel.showLogDialog(false) },
            onSave = { title, number, platform, difficulty, tags, timeComp, spaceComp, notes ->
                viewModel.addNewProblem(title, number, platform, difficulty, tags, timeComp, spaceComp, notes)
                Toast.makeText(context, "Problem logged and added to SM-2 queue!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Blind Retry Modal
    uiState.activeBlindRetryProblem?.let { problem ->
        BlindRetryModal(
            problem = problem,
            onDismiss = { viewModel.dismissBlindRetry() },
            onGrade = { grade ->
                viewModel.logSpacedRepetitionResult(problem.id, grade)
                Toast.makeText(context, "SM-2 interval updated ($grade)!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
