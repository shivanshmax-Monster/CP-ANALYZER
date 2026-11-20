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
import androidx.compose.runtime.*
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
import com.example.ui.components.DifficultyBadge
import com.example.ui.theme.*
import com.example.viewmodel.AlgoUiState

@Composable
fun RevisitScreen(
    uiState: AlgoUiState,
    onStartBlindRetry: (ProblemItem) -> Unit,
    onLogSM2Result: (String, String) -> Unit,
    onMarkMastered: (String) -> Unit,
    onSnooze: (String, Int) -> Unit,
    onFilterChange: (String) -> Unit,
    onToggleBlindMode: (Boolean) -> Unit,
    onTogglePeekHint: () -> Unit,
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
        // Spaced Repetition Queue Header
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "💡", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "MEMORY CURVE DECAY",
                        color = AmberGold,
                        fontFamily = JetBrainsMono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SlateElevation,
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, SlateBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(AmberGold)
                        )
                        Text(
                            text = "SM-2 Engine v2.4",
                            color = TextMediumEmphasis,
                            fontFamily = JetBrainsMono,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Text(
                text = "Spaced Repetition Queue",
                color = TextHighEmphasis,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Algorithmic retention powered by SM-2 intervals (3d → 7d → 21d → 45d → Mastered)",
                color = TextMediumEmphasis,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Retention Index & Next Session Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = DeepZincCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Circular Gauge 78%
                    Box(
                        modifier = Modifier.size(54.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(50.dp)) {
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
                                sweepAngle = 280f, // 78%
                                useCenter = false,
                                style = Stroke(stroke, cap = StrokeCap.Round)
                            )
                        }
                        Text(
                            text = "78%",
                            color = NeonEmerald,
                            fontFamily = JetBrainsMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Column {
                        Text(
                            text = "Retention Index",
                            color = TextHighEmphasis,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Ebbinghaus slope stabilized (+4.2%)",
                            color = TextMediumEmphasis,
                            fontSize = 11.sp
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "NEXT SESSION",
                        color = AmberGold,
                        fontFamily = JetBrainsMono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "14:00 Today",
                        color = TextHighEmphasis,
                        fontFamily = JetBrainsMono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Filter chips row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filters = listOf(
                "Due Today" to "Due Today (4)",
                "Stage 1" to "Stage 1: 3 Days (6)",
                "Stage 2" to "Stage 2: 7 Days (8)"
            )

            filters.forEach { (key, label) ->
                val selected = uiState.revisitFilter == key
                Surface(
                    modifier = Modifier.clickable { onFilterChange(key) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (selected) AmberGold else DeepZincCard,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (selected) AmberGold else SlateBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (selected) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(ObsidianBg)
                            )
                        }
                        Text(
                            text = label,
                            color = if (selected) ObsidianBg else TextMediumEmphasis,
                            fontSize = 12.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Review Mode Toggle
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = DeepZincCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Segmented buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SlateElevation)
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Standard Review
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (!uiState.isBlindRetryMode) DeepZincCard else Color.Transparent)
                            .clickable { onToggleBlindMode(false) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Standard Review",
                            color = if (!uiState.isBlindRetryMode) TextHighEmphasis else TextMediumEmphasis,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Blind Retry Mode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (uiState.isBlindRetryMode) LightIndigo else Color.Transparent)
                            .clickable { onToggleBlindMode(true) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Blind Retry Mode",
                                color = if (uiState.isBlindRetryMode) Color(0xFF1000A9) else TextMediumEmphasis,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (uiState.isBlindRetryMode) Color(0xFF1000A9) else TextMediumEmphasis,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }

                // Strict Recall explainer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = NeonEmerald,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "Strict Recall: Hides all hints, previous complexity analysis, and code snippets for a pure whiteboarding rehearsal.",
                        color = TextMediumEmphasis,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Urgent Queue Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "URGENT QUEUE",
                    color = TextHighEmphasis,
                    fontFamily = JetBrainsMono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(AmberGold)
                )
            }
            Text(
                text = "4 items requiring action",
                color = TextMediumEmphasis,
                fontSize = 11.sp
            )
        }

        // Alien Dictionary Card
        AlienDictionaryCard(
            problem = uiState.problems.firstOrNull { it.id == "lc-269" } ?: uiState.problems.first(),
            peekHintActive = uiState.peekHintActive,
            onStartBlindRetry = { onStartBlindRetry(it) },
            onTogglePeekHint = onTogglePeekHint,
            onPostpone = { onSnooze(it.id, 1) },
            onMarkMastered = { onMarkMastered(it.id) },
            modifier = Modifier.fillMaxWidth()
        )

        // Edit Distance Card
        EditDistanceCard(
            problem = uiState.problems.firstOrNull { it.id == "lc-72" } ?: uiState.problems[1],
            onLogSM2Result = onLogSM2Result,
            onStartBlindRetry = { onStartBlindRetry(it) },
            modifier = Modifier.fillMaxWidth()
        )

        // Mini Cards: Maximum Subarray & Binary Tree Max Path Sum
        MiniProblemCard(
            problem = uiState.problems.firstOrNull { it.id == "lc-53" } ?: uiState.problems[2],
            onPlay = { onStartBlindRetry(it) },
            modifier = Modifier.fillMaxWidth()
        )

        MiniProblemCard(
            problem = uiState.problems.firstOrNull { it.id == "lc-124" } ?: uiState.problems[3],
            onPlay = { onStartBlindRetry(it) },
            modifier = Modifier.fillMaxWidth()
        )

        // Why SM-2 Spaced Repetition? Section
        WhySM2Card(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun AlienDictionaryCard(
    problem: ProblemItem,
    peekHintActive: Boolean,
    onStartBlindRetry: (ProblemItem) -> Unit,
    onTogglePeekHint: () -> Unit,
    onPostpone: (ProblemItem) -> Unit,
    onMarkMastered: (ProblemItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = DeepZincCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Badges row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = SlateElevation
                    ) {
                        Text(
                            text = problem.number,
                            color = TextMediumEmphasis,
                            fontFamily = JetBrainsMono,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    DifficultyBadge(difficulty = problem.difficulty)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AmberBg,
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, AmberGold.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "Due Today",
                            color = AmberGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
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

            Text(
                text = problem.tags.joinToString(" • "),
                color = TextMediumEmphasis,
                fontSize = 11.sp
            )

            // Progress bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Update,
                            contentDescription = null,
                            tint = NeonEmerald,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = problem.stageText,
                            color = TextMediumEmphasis,
                            fontSize = 11.sp
                        )
                    }
                    Text(
                        text = "Mastery ${problem.masteryPercent}%",
                        color = NeonEmerald,
                        fontFamily = JetBrainsMono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                LinearProgressIndicator(
                    progress = { problem.masteryPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = NeonEmerald,
                    trackColor = SlateElevation
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${problem.consecutiveRecalls} consecutive clean recalls",
                        color = TextMediumEmphasis,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Decaying in 4h",
                        color = AmberGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Stumble memo or peek hint
            if (problem.pastStumbleMemo != null || peekHintActive) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SlateElevation.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, SlateBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = AmberGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PAST STUMBLE MEMO",
                                color = AmberGold,
                                fontFamily = JetBrainsMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = "\"${problem.pastStumbleMemo}\"",
                            color = TextHighEmphasis,
                            fontSize = 11.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                        if (peekHintActive && problem.intuitionNote != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Hint: ${problem.intuitionNote}",
                                color = LightIndigo,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Action: Start Blind Retry (green)
            Button(
                onClick = { onStartBlindRetry(problem) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonEmerald,
                    contentColor = ObsidianBg
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.RocketLaunch,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Start Blind Retry",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            // Peek Hint & Postpone row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onTogglePeekHint,
                    modifier = Modifier.weight(1f).height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextHighEmphasis,
                        containerColor = SlateElevation
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = null,
                        tint = AmberGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (peekHintActive) "Hide Hint" else "Peek Hint",
                        fontSize = 11.sp
                    )
                }

                OutlinedButton(
                    onClick = { onPostpone(problem) },
                    modifier = Modifier.weight(1f).height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextHighEmphasis,
                        containerColor = SlateElevation
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Postpone (+1d)", fontSize = 11.sp)
                }
            }

            // Passed: Mark Mastered button
            Button(
                onClick = { onMarkMastered(problem) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightIndigo,
                    contentColor = Color(0xFF1000A9)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Passed: Mark Mastered",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun EditDistanceCard(
    problem: ProblemItem,
    onLogSM2Result: (String, String) -> Unit,
    onStartBlindRetry: (ProblemItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = DeepZincCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Badges row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = SlateElevation
                    ) {
                        Text(
                            text = problem.number,
                            color = TextMediumEmphasis,
                            fontFamily = JetBrainsMono,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    DifficultyBadge(difficulty = problem.difficulty)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CrimsonBg,
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, CrimsonRed.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Blind Spot 🔥",
                                color = CrimsonRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
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

            Text(
                text = problem.tags.joinToString(" • "),
                color = TextMediumEmphasis,
                fontSize = 11.sp
            )

            // Cadence progress
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = AmberGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = problem.stageText,
                            color = TextMediumEmphasis,
                            fontSize = 11.sp
                        )
                    }
                    Text(
                        text = "Mastery ${problem.masteryPercent}%",
                        color = AmberGold,
                        fontFamily = JetBrainsMono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                LinearProgressIndicator(
                    progress = { problem.masteryPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = AmberGold,
                    trackColor = SlateElevation
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Failed last retry on memoization base case",
                        color = CrimsonRed,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Due Today",
                        color = AmberGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // LOG SPACED REPETITION RESULT Header
            Text(
                text = "LOG SPACED REPETITION RESULT:",
                color = TextMediumEmphasis,
                fontFamily = JetBrainsMono,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )

            // 4 SM-2 Rating Buttons: Fail 1d, Hard 3d, Good 7d, Easy 14d
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Fail
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onLogSM2Result(problem.id, "Fail") },
                    shape = RoundedCornerShape(6.dp),
                    color = SlateElevation,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Fail", color = TextHighEmphasis, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("1d", color = TextMediumEmphasis, fontFamily = JetBrainsMono, fontSize = 10.sp)
                    }
                }

                // Hard
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onLogSM2Result(problem.id, "Hard") },
                    shape = RoundedCornerShape(6.dp),
                    color = SlateElevation,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Hard", color = AmberGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("3d", color = TextMediumEmphasis, fontFamily = JetBrainsMono, fontSize = 10.sp)
                    }
                }

                // Good
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onLogSM2Result(problem.id, "Good") },
                    shape = RoundedCornerShape(6.dp),
                    color = SlateElevation,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Good", color = LightIndigo, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("7d", color = TextMediumEmphasis, fontFamily = JetBrainsMono, fontSize = 10.sp)
                    }
                }

                // Easy
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onLogSM2Result(problem.id, "Easy") },
                    shape = RoundedCornerShape(6.dp),
                    color = EmeraldGreen,
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Easy", color = ObsidianBg, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("14d", color = ObsidianBg, fontFamily = JetBrainsMono, fontSize = 10.sp)
                    }
                }
            }

            // LeetCode & In-App Code Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* external link */ },
                    modifier = Modifier.weight(1f).height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextHighEmphasis,
                        containerColor = SlateElevation
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Solve on LeetCode", fontSize = 11.sp)
                }

                Button(
                    onClick = { onStartBlindRetry(problem) },
                    modifier = Modifier.weight(1f).height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightIndigo,
                        contentColor = Color(0xFF1000A9)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("In-App Code", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MiniProblemCard(
    problem: ProblemItem,
    onPlay: (ProblemItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = DeepZincCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = problem.number,
                        color = TextMediumEmphasis,
                        fontFamily = JetBrainsMono,
                        fontSize = 11.sp
                    )
                    DifficultyBadge(difficulty = problem.difficulty)
                    Text(
                        text = problem.dueStatus,
                        color = TextMediumEmphasis,
                        fontSize = 10.sp
                    )
                }

                Text(
                    text = problem.title,
                    color = TextHighEmphasis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Text(
                    text = "${problem.tags.firstOrNull() ?: ""} • ${problem.complexityTime}",
                    color = TextMediumEmphasis,
                    fontSize = 11.sp
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { onPlay(problem) },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SlateElevation)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Review",
                        tint = TextHighEmphasis,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${problem.masteryPercent}%",
                    color = if (problem.masteryPercent > 70) NeonEmerald else AmberGold,
                    fontFamily = JetBrainsMono,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun WhySM2Card(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = DeepZincCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Insights,
                    contentDescription = null,
                    tint = LightIndigo,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Why SM-2 Spaced Repetition?",
                    color = TextHighEmphasis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Text(
                text = "Human memory naturally follows an exponential decay. Revisiting problems right as forgetting begins flattens your decay curve, transforming high-difficulty graph and dynamic programming solutions into permanent neural muscle memory.",
                color = TextMediumEmphasis,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            // 5 Stages
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val stages = listOf(
                    Triple("1st", "Day 1", "Learn"),
                    Triple("2nd", "3 Days", "Reinforce"),
                    Triple("3rd", "7 Days", "Consolidate"),
                    Triple("4th", "21 Days", "Mastery"),
                    Triple("5th", "45 Days", "Vault")
                )

                stages.forEachIndexed { index, (order, interval, purpose) ->
                    val isFinal = index == 4
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = if (isFinal) NeonEmerald else SlateElevation,
                        border = androidx.compose.foundation.BorderStroke(
                            0.8.dp,
                            if (isFinal) NeonEmerald else SlateBorder
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = order,
                                color = if (isFinal) ObsidianBg else TextMediumEmphasis,
                                fontSize = 9.sp
                            )
                            Text(
                                text = interval,
                                color = if (isFinal) ObsidianBg else TextHighEmphasis,
                                fontFamily = JetBrainsMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = purpose,
                                color = if (isFinal) ObsidianBg else TextMediumEmphasis,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Target Interview Readiness: 98.4%",
                    color = TextMediumEmphasis,
                    fontSize = 11.sp
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Read Algorithm Whitepaper",
                        color = LightIndigo,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = LightIndigo,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
