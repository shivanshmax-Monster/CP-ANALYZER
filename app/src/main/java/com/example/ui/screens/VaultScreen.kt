package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Difficulty
import com.example.model.ProblemItem
import com.example.ui.components.ComplexityTag
import com.example.ui.components.DifficultyBadge
import com.example.ui.theme.*
import com.example.viewmodel.AlgoUiState

@Composable
fun VaultScreen(
    uiState: AlgoUiState,
    onSearchChange: (String) -> Unit,
    onPlatformFilterChange: (String) -> Unit,
    onStatusFilterChange: (String?) -> Unit,
    onReviewProblem: (ProblemItem) -> Unit,
    onExportMarkdown: () -> Unit,
    onSyncGithub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val filteredProblems = remember(
        uiState.problems,
        uiState.vaultSearchQuery,
        uiState.vaultPlatformFilter,
        uiState.vaultStatusFilter
    ) {
        uiState.problems.filter { p ->
            val matchesSearch = uiState.vaultSearchQuery.isBlank() ||
                    p.title.contains(uiState.vaultSearchQuery, ignoreCase = true) ||
                    p.number.contains(uiState.vaultSearchQuery, ignoreCase = true) ||
                    p.tags.any { it.contains(uiState.vaultSearchQuery, ignoreCase = true) }
            val matchesPlatform = uiState.vaultPlatformFilter == "All" ||
                    p.platform.equals(uiState.vaultPlatformFilter, ignoreCase = true)
            val matchesStatus = when (uiState.vaultStatusFilter) {
                "Mastered" -> p.isMastered
                "Needs Review" -> p.isDueToday || p.dueStatus.contains("Review", ignoreCase = true)
                "Critical Blind Spot" -> p.isBlindSpot
                else -> true
            }
            matchesSearch && matchesPlatform && matchesStatus
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search Bar
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = DeepZincCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextMediumEmphasis,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                TextField(
                    value = uiState.vaultSearchQuery,
                    onValueChange = onSearchChange,
                    placeholder = {
                        Text(
                            text = "Search 648 solved problems by title, tag, intu...",
                            color = TextMediumEmphasis,
                            fontSize = 12.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = TextHighEmphasis,
                        unfocusedTextColor = TextHighEmphasis
                    ),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = SlateElevation,
                    border = androidx.compose.foundation.BorderStroke(0.6.dp, SlateBorder)
                ) {
                    Text(
                        text = "⌘K",
                        color = TextMediumEmphasis,
                        fontFamily = JetBrainsMono,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        // Platform Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val platforms = listOf(
                "All" to "All Platforms (648)",
                "LeetCode" to "LeetCode 412",
                "Codeforces" to "Codeforces 156",
                "GeeksForGeeks" to "GeeksForGeeks 80"
            )

            platforms.forEach { (key, label) ->
                val selected = uiState.vaultPlatformFilter == key
                Surface(
                    modifier = Modifier.clickable { onPlatformFilterChange(key) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (selected) LightIndigo else DeepZincCard,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (selected) LightIndigo else SlateBorder
                    )
                ) {
                    Text(
                        text = label,
                        color = if (selected) Color(0xFF1000A9) else TextMediumEmphasis,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }
            }
        }

        // Status Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val statuses = listOf(
                Triple("Mastered", "Mastered", NeonEmerald),
                Triple("Needs Review", "Needs Review", AmberGold),
                Triple("Critical Blind Spot", "Critical Blind Spot", CrimsonRed)
            )

            statuses.forEach { (key, label, dotColor) ->
                val selected = uiState.vaultStatusFilter == key
                Surface(
                    modifier = Modifier.clickable { onStatusFilterChange(key) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (selected) dotColor.copy(alpha = 0.2f) else DeepZincCard,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (selected) dotColor else SlateBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(dotColor)
                        )
                        Text(
                            text = label,
                            color = if (selected) TextHighEmphasis else TextMediumEmphasis,
                            fontSize = 11.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Section header: Problem Dossiers [SM-2 Sync On] • Last Solved
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Problem Dossiers",
                    color = TextHighEmphasis,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = SlateElevation
                ) {
                    Text(
                        text = "SM-2 Sync On",
                        color = TextMediumEmphasis,
                        fontFamily = JetBrainsMono,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = null,
                    tint = TextMediumEmphasis,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Last Solved",
                    color = TextMediumEmphasis,
                    fontSize = 11.sp
                )
            }
        }

        // Problem Dossier Cards
        filteredProblems.forEach { problem ->
            ProblemDossierCard(
                problem = problem,
                onReview = { onReviewProblem(problem) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Curated Vault Balance Card
        CuratedVaultBalanceCard(
            total = uiState.totalSolved,
            easy = uiState.easySolved,
            med = uiState.medSolved,
            hard = uiState.hardSolved,
            modifier = Modifier.fillMaxWidth()
        )

        // Bottom Actions
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SlateElevation,
                border = androidx.compose.foundation.BorderStroke(0.8.dp, SlateBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(NeonEmerald)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Showing ${filteredProblems.size} Solved Problems",
                        color = TextMediumEmphasis,
                        fontFamily = JetBrainsMono,
                        fontSize = 11.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onExportMarkdown,
                    modifier = Modifier.weight(1f).height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextHighEmphasis,
                        containerColor = SlateElevation
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Export Markdown", fontSize = 11.sp)
                }

                Button(
                    onClick = onSyncGithub,
                    modifier = Modifier.weight(1f).height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SlateElevation,
                        contentColor = TextHighEmphasis
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Sync to GitHub Repo", fontSize = 11.sp)
                }
            }

            Text(
                text = "Continuous local cryptographic backups enabled • AlgoSync Vault v2.4",
                color = TextDisabled,
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProblemDossierCard(
    problem: ProblemItem,
    onReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedCode by remember { mutableStateOf(problem.codeSnippet != null) }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = DeepZincCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header: Platform number, Difficulty, time ago, duration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = AmberBg,
                        border = androidx.compose.foundation.BorderStroke(0.6.dp, AmberGold.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = problem.number,
                            color = AmberGold,
                            fontFamily = JetBrainsMono,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    DifficultyBadge(difficulty = problem.difficulty)
                    Text(
                        text = "• ${problem.timeAgo}",
                        color = TextMediumEmphasis,
                        fontSize = 11.sp
                    )
                }

                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = null,
                    tint = TextMediumEmphasis,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = problem.title,
                color = TextHighEmphasis,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            // Intuition quote block
            if (problem.intuitionNote != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SlateElevation.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(0.6.dp, SlateBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Lightbulb,
                            contentDescription = null,
                            tint = AmberGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "\"${problem.intuitionNote}\"",
                            color = TextHighEmphasis,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }

            // Complexity & tags row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ComplexityTag(icon = Icons.Outlined.Schedule, text = problem.complexityTime)
                ComplexityTag(icon = Icons.Outlined.Storage, text = problem.complexitySpace)
                problem.tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SlateElevation)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "#$tag",
                            color = TextMediumEmphasis,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Code Snippet section
            if (problem.codeSnippet != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF090D14),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, SlateBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Code,
                                    contentDescription = null,
                                    tint = LightIndigo,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = problem.beatsSummary ?: "Optimized Solution",
                                    color = TextHighEmphasis,
                                    fontFamily = JetBrainsMono,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Text(
                            text = problem.codeSnippet,
                            color = LightIndigo,
                            fontFamily = JetBrainsMono,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Footer: Status and Review button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (problem.isMastered) NeonEmerald else AmberGold)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (problem.isMastered) "Mastered (${problem.stageText})" else problem.dueStatus,
                        color = if (problem.isMastered) NeonEmerald else AmberGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (problem.isMastered) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = TextMediumEmphasis,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "SM-2: ${problem.sm2EaseFactor}",
                            color = TextMediumEmphasis,
                            fontFamily = JetBrainsMono,
                            fontSize = 11.sp
                        )
                    }
                } else {
                    Button(
                        onClick = onReview,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (problem.dueStatus.contains("Tomorrow")) LightIndigo else SlateElevation,
                            contentColor = if (problem.dueStatus.contains("Tomorrow")) Color(0xFF1000A9) else TextHighEmphasis
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = if (problem.dueStatus.contains("Tomorrow")) "⚡ Review Now" else "Log Review",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CuratedVaultBalanceCard(
    total: Int,
    easy: Int,
    med: Int,
    hard: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = DeepZincCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DonutLarge,
                        contentDescription = null,
                        tint = NeonEmerald,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Curated Vault Balance",
                        color = TextHighEmphasis,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Text(
                    text = "$total Solved",
                    color = TextMediumEmphasis,
                    fontFamily = JetBrainsMono,
                    fontSize = 11.sp
                )
            }

            // Tri-color segmented bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .weight(easy.toFloat())
                        .fillMaxHeight()
                        .background(NeonEmerald)
                )
                Box(
                    modifier = Modifier
                        .weight(med.toFloat())
                        .fillMaxHeight()
                        .background(AmberGold)
                )
                Box(
                    modifier = Modifier
                        .weight(hard.toFloat())
                        .fillMaxHeight()
                        .background(LightCoral)
                )
            }

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(NeonEmerald))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Easy $easy", color = TextMediumEmphasis, fontSize = 11.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(AmberGold))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Med $med", color = TextMediumEmphasis, fontSize = 11.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(LightCoral))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Hard $hard", color = TextMediumEmphasis, fontSize = 11.sp)
                }
            }
        }
    }
}
