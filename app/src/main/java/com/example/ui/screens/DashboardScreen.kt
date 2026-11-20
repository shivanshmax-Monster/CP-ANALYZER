package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Difficulty
import com.example.model.ProblemItem
import com.example.model.SampleData
import com.example.ui.components.ComplexityTag
import com.example.ui.components.DifficultyBadge
import com.example.ui.theme.*
import com.example.viewmodel.AlgoUiState

@Composable
fun DashboardScreen(
    uiState: AlgoUiState,
    onStartBlindRetry: (ProblemItem) -> Unit,
    onSnooze: (String, Int) -> Unit,
    onMarkMastered: (String) -> Unit,
    onOpenLog: () -> Unit,
    onNavigateToRevisit: () -> Unit,
    onNavigateToVault: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Daily Circuit Card
        DailyCircuitCard(
            uiState = uiState,
            modifier = Modifier.fillMaxWidth()
        )

        // Weakness Alert Banner
        WeaknessAlertBanner(
            modifier = Modifier.fillMaxWidth(),
            onBannerClick = onNavigateToRevisit
        )

        // Priority Revisit Queue
        PriorityRevisitSection(
            problems = uiState.problems.filter { it.isDueToday },
            onStartBlindRetry = onStartBlindRetry,
            onSnooze = onSnooze,
            onMarkMastered = onMarkMastered,
            onViewAll = onNavigateToRevisit,
            modifier = Modifier.fillMaxWidth()
        )

        // Recent Synced Solves
        RecentSyncedSolvesSection(
            problems = uiState.problems.take(2),
            onViewAll = onNavigateToVault,
            modifier = Modifier.fillMaxWidth()
        )

        // Quick Log Solve Card
        QuickLogSolveCard(
            onClick = onOpenLog,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun DailyCircuitCard(
    uiState: AlgoUiState,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = DeepZincCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: DAILY CIRCUIT and Circular Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "DAILY CIRCUIT",
                            color = NeonEmerald,
                            fontFamily = JetBrainsMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(NeonEmerald)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Today's Target",
                        color = TextHighEmphasis,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "2 Revisions due + 1 New Problem",
                        color = TextMediumEmphasis,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Circular Progress 2/3 (66%)
                Box(
                    modifier = Modifier.size(58.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(54.dp)) {
                        val stroke = 5.dp.toPx()
                        drawArc(
                            color = SlateElevation,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(stroke)
                        )
                        drawArc(
                            color = NeonEmerald,
                            startAngle = -90f,
                            sweepAngle = 240f, // 66%
                            useCenter = false,
                            style = Stroke(stroke, cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${uiState.todayTargetCompleted}/${uiState.todayTargetTotal}",
                            color = TextHighEmphasis,
                            fontFamily = JetBrainsMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "66%",
                            color = NeonEmerald,
                            fontFamily = JetBrainsMono,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            // 3 Stat Boxes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Total Solved
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = SlateElevation.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, SlateBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Total Solved",
                            color = TextMediumEmphasis,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${uiState.totalSolved}",
                            color = TextHighEmphasis,
                            fontFamily = JetBrainsMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("E${uiState.easySolved}", color = EmeraldGreen, fontFamily = JetBrainsMono, fontSize = 9.sp)
                            Text("M${uiState.medSolved}", color = AmberGold, fontFamily = JetBrainsMono, fontSize = 9.sp)
                            Text("H${uiState.hardSolved}", color = CrimsonRed, fontFamily = JetBrainsMono, fontSize = 9.sp)
                        }
                    }
                }

                // Mastery Streak
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = SlateElevation.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, SlateBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Mastery Streak",
                            color = TextMediumEmphasis,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${uiState.streakDays}d",
                                color = AmberGold,
                                fontFamily = JetBrainsMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("🔥", fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Cross-platform",
                            color = TextMediumEmphasis,
                            fontSize = 10.sp
                        )
                    }
                }

                // Next Due
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = SlateElevation.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, SlateBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Next Due",
                            color = TextMediumEmphasis,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "In 2h",
                            color = TextHighEmphasis,
                            fontFamily = JetBrainsMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "3 pending",
                            color = TextMediumEmphasis,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            // 12-week Sync Cadence Heatmap
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = SlateElevation.copy(alpha = 0.4f),
                border = androidx.compose.foundation.BorderStroke(0.8.dp, SlateBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timeline,
                                contentDescription = null,
                                tint = NeonEmerald,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Sync Cadence (12 Wk)",
                                color = TextHighEmphasis,
                                fontFamily = JetBrainsMono,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = "${uiState.retentionPercent}% Retention",
                            color = NeonEmerald,
                            fontFamily = JetBrainsMono,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val cadenceDays = remember { SampleData.generateCadenceGrid() }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (w in 0 until 12) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                for (d in 0 until 4) {
                                    val item = cadenceDays.firstOrNull { it.week == w && it.dayOfWeek == d }
                                    val color = when (item?.level ?: 0) {
                                        3 -> NeonEmerald
                                        2 -> EmeraldGreen
                                        1 -> EmeraldGreen.copy(alpha = 0.4f)
                                        else -> SlateElevation
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(9.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(color)
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

@Composable
fun WeaknessAlertBanner(
    onBannerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable { onBannerClick() },
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF1E1116),
        border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonRed.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.WarningAmber,
                contentDescription = "Alert",
                tint = CrimsonRed,
                modifier = Modifier.size(20.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Weakness Alert",
                        color = TextHighEmphasis,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(CrimsonRed)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "DP: 38% Fail",
                            color = Color.White,
                            fontFamily = JetBrainsMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }

                Text(
                    text = "Dynamic Programming retry failure rate is elevated. 4 problems scheduled for spaced recall this week.",
                    color = TextMediumEmphasis,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun PriorityRevisitSection(
    problems: List<ProblemItem>,
    onStartBlindRetry: (ProblemItem) -> Unit,
    onSnooze: (String, Int) -> Unit,
    onMarkMastered: (String) -> Unit,
    onViewAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = LightIndigo,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Priority Revisit Queue",
                    color = TextHighEmphasis,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "SM-2 Cadence",
                color = TextMediumEmphasis,
                fontFamily = JetBrainsMono,
                fontSize = 11.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            problems.take(3).forEach { problem ->
                PriorityRevisitCard(
                    problem = problem,
                    onBlindRetry = { onStartBlindRetry(problem) },
                    onSnooze = { onSnooze(problem.id, 2) },
                    onMastered = { onMarkMastered(problem.id) },
                    modifier = Modifier.width(300.dp)
                )
            }
        }
    }
}

@Composable
fun PriorityRevisitCard(
    problem: ProblemItem,
    onBlindRetry: () -> Unit,
    onSnooze: () -> Unit,
    onMastered: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = DeepZincCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = problem.number,
                    color = TextMediumEmphasis,
                    fontFamily = JetBrainsMono,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp
                )
                DifficultyBadge(difficulty = problem.difficulty)
            }

            Text(
                text = problem.title,
                color = TextHighEmphasis,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                problem.tags.take(2).forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SlateElevation)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = tag,
                            color = TextMediumEmphasis,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = AmberGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = problem.stageText,
                        color = AmberGold,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = problem.dueStatus,
                    color = NeonEmerald,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Blind Retry Button
            Button(
                onClick = onBlindRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightIndigo,
                    contentColor = Color(0xFF1000A9)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .testTag("blind_retry_${problem.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Blind Retry",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            // Snooze & Mastered row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onSnooze,
                    modifier = Modifier.weight(1f).height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextHighEmphasis,
                        containerColor = SlateElevation
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Text("+2d Snooze", fontSize = 11.sp)
                }

                Button(
                    onClick = onMastered,
                    modifier = Modifier.weight(1f).height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldBg,
                        contentColor = NeonEmerald
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonEmerald.copy(alpha = 0.3f))
                ) {
                    Text("Mastered", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RecentSyncedSolvesSection(
    problems: List<ProblemItem>,
    onViewAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Synced Solves",
                color = TextHighEmphasis,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View All",
                color = LightIndigo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                modifier = Modifier.clickable { onViewAll() }
            )
        }

        problems.forEach { problem ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = DeepZincCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Icon box
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SlateElevation),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = null,
                            tint = if (problem.platform == "Codeforces") LightIndigo else AmberGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = problem.title,
                                color = TextHighEmphasis,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                maxLines = 1
                            )
                            if (problem.difficulty == Difficulty.HARD) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CrimsonBg)
                                        .border(0.6.dp, CrimsonRed.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "Hard",
                                        color = CrimsonRed,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = "${problem.number} • ${problem.complexityTime} • ${problem.complexitySpace}",
                            color = TextMediumEmphasis,
                            fontFamily = JetBrainsMono,
                            fontSize = 10.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = problem.timeAgo,
                            color = TextMediumEmphasis,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Solved",
                            tint = NeonEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickLogSolveCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable { onClick() }.testTag("quick_log_card"),
        shape = RoundedCornerShape(12.dp),
        color = DeepZincCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(LightIndigo),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = Color(0xFF1000A9),
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Quick Log Solve",
                    color = TextHighEmphasis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Sync via URL, Snippet, or Auto-detect",
                    color = TextMediumEmphasis,
                    fontSize = 11.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "⌘K",
                    color = TextMediumEmphasis,
                    fontFamily = JetBrainsMono,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = TextMediumEmphasis,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
