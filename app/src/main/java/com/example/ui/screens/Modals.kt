package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.window.Dialog
import com.example.model.Difficulty
import com.example.model.ProblemItem
import com.example.ui.components.DifficultyBadge
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun QuickLogDialog(
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        number: String,
        platform: String,
        difficulty: Difficulty,
        tags: List<String>,
        timeComp: String,
        spaceComp: String,
        notes: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("LeetCode") }
    var difficulty by remember { mutableStateOf(Difficulty.MEDIUM) }
    var tagsInput by remember { mutableStateOf("") }
    var timeComp by remember { mutableStateOf("O(N)") }
    var spaceComp by remember { mutableStateOf("O(1)") }
    var notes by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DeepZincCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(LightIndigo),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color(0xFF1000A9),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Log New Solve",
                            color = TextHighEmphasis,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMediumEmphasis,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Problem Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Problem Title", color = TextMediumEmphasis) },
                    placeholder = { Text("e.g. Longest Consecutive Sequence", color = TextDisabled) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextHighEmphasis,
                        unfocusedTextColor = TextHighEmphasis,
                        focusedBorderColor = ElectricIndigo,
                        unfocusedBorderColor = SlateBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("log_title_input")
                )

                // Number / ID
                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Problem Number / Ref", color = TextMediumEmphasis) },
                    placeholder = { Text("e.g. LC #128", color = TextDisabled) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextHighEmphasis,
                        unfocusedTextColor = TextHighEmphasis,
                        focusedBorderColor = ElectricIndigo,
                        unfocusedBorderColor = SlateBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Platform selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("LeetCode", "Codeforces", "GeeksForGeeks").forEach { plat ->
                        val selected = platform == plat
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selected) LightIndigo else SlateElevation)
                                .clickable { platform = plat }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = plat,
                                color = if (selected) Color(0xFF1000A9) else TextHighEmphasis,
                                fontSize = 11.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Difficulty selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Difficulty.entries.forEach { diff ->
                        val selected = difficulty == diff
                        val color = when (diff) {
                            Difficulty.EASY -> EmeraldGreen
                            Difficulty.MEDIUM -> AmberGold
                            Difficulty.HARD -> CrimsonRed
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selected) color.copy(alpha = 0.25f) else SlateElevation)
                                .border(
                                    1.dp,
                                    if (selected) color else SlateBorder,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { difficulty = diff }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = diff.label,
                                color = if (selected) color else TextMediumEmphasis,
                                fontSize = 12.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Tags
                OutlinedTextField(
                    value = tagsInput,
                    onValueChange = { tagsInput = it },
                    label = { Text("Tags (comma-separated)", color = TextMediumEmphasis) },
                    placeholder = { Text("Array, Hash Table", color = TextDisabled) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextHighEmphasis,
                        unfocusedTextColor = TextHighEmphasis,
                        focusedBorderColor = ElectricIndigo,
                        unfocusedBorderColor = SlateBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Complexities
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = timeComp,
                        onValueChange = { timeComp = it },
                        label = { Text("Time", color = TextMediumEmphasis) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextHighEmphasis,
                            unfocusedTextColor = TextHighEmphasis,
                            focusedBorderColor = ElectricIndigo,
                            unfocusedBorderColor = SlateBorder
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = spaceComp,
                        onValueChange = { spaceComp = it },
                        label = { Text("Space", color = TextMediumEmphasis) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextHighEmphasis,
                            unfocusedTextColor = TextHighEmphasis,
                            focusedBorderColor = ElectricIndigo,
                            unfocusedBorderColor = SlateBorder
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Key Intuition Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Intuition & Flashcard Memo", color = TextMediumEmphasis) },
                    placeholder = { Text("Core invariant, tricky edge case...", color = TextDisabled) },
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextHighEmphasis,
                        unfocusedTextColor = TextHighEmphasis,
                        focusedBorderColor = ElectricIndigo,
                        unfocusedBorderColor = SlateBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextHighEmphasis,
                            containerColor = SlateElevation
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                    ) {
                        Text("Cancel", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                val tagList = tagsInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                                onSave(title, number, platform, difficulty, tagList, timeComp, spaceComp, notes)
                            }
                        },
                        modifier = Modifier.weight(1f).height(42.dp).testTag("save_problem_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightIndigo,
                            contentColor = Color(0xFF1000A9)
                        ),
                        enabled = title.isNotBlank()
                    ) {
                        Text("Save to Vault", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BlindRetryModal(
    problem: ProblemItem,
    onDismiss: () -> Unit,
    onGrade: (String) -> Unit
) {
    var secondsElapsed by remember { mutableStateOf(0) }
    var isRevealed by remember { mutableStateOf(false) }
    var scratchpadText by remember { mutableStateOf("") }

    // Timer effect
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            secondsElapsed++
        }
    }

    val minutes = secondsElapsed / 60
    val seconds = secondsElapsed % 60
    val timeFormatted = "%02d:%02d".format(minutes, seconds)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DeepZincCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Top Header: Blind Retry Mode & Timer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(AmberGold)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "BLIND RETRY MODE",
                            color = AmberGold,
                            fontFamily = JetBrainsMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SlateElevation,
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, SlateBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = NeonEmerald,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = timeFormatted,
                                color = TextHighEmphasis,
                                fontFamily = JetBrainsMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Problem title and badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = problem.number,
                            color = TextMediumEmphasis,
                            fontFamily = JetBrainsMono,
                            fontSize = 11.sp
                        )
                        Text(
                            text = problem.title,
                            color = TextHighEmphasis,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    DifficultyBadge(difficulty = problem.difficulty)
                }

                // Tags
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    problem.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SlateElevation)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(text = tag, color = TextMediumEmphasis, fontSize = 11.sp)
                        }
                    }
                }

                // Whiteboard / Scratchpad
                Text(
                    text = "Mental Whiteboard / Algorithm Outline:",
                    color = TextMediumEmphasis,
                    fontSize = 11.sp
                )

                OutlinedTextField(
                    value = scratchpadText,
                    onValueChange = { scratchpadText = it },
                    placeholder = {
                        Text(
                            "Type your algorithm approach, edge cases, base cases, and data structure choice before checking solution...",
                            color = TextDisabled,
                            fontSize = 12.sp
                        )
                    },
                    minLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextHighEmphasis,
                        unfocusedTextColor = TextHighEmphasis,
                        focusedBorderColor = ElectricIndigo,
                        unfocusedBorderColor = SlateBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("scratchpad_input")
                )

                // Solution reveal section
                if (!isRevealed) {
                    Button(
                        onClick = { isRevealed = true },
                        modifier = Modifier.fillMaxWidth().height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SlateElevation,
                            contentColor = LightIndigo
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                    ) {
                        Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reveal Solution & Reference Intuition", fontSize = 12.sp)
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF090D14),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonEmerald.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Reference Intuition:",
                                color = NeonEmerald,
                                fontFamily = JetBrainsMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = problem.intuitionNote ?: "Two pointer or dynamic programming state tracking.",
                                color = TextHighEmphasis,
                                fontSize = 12.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                            if (problem.codeSnippet != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = problem.codeSnippet,
                                    color = LightIndigo,
                                    fontFamily = JetBrainsMono,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                // Self-grading prompt
                Text(
                    text = "RATE YOUR RECALL TO UPDATE SM-2 INTERVAL:",
                    color = TextMediumEmphasis,
                    fontFamily = JetBrainsMono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val grades = listOf(
                        Triple("Fail", "1d", CrimsonRed),
                        Triple("Hard", "3d", AmberGold),
                        Triple("Good", "7d", LightIndigo),
                        Triple("Easy", "14d", NeonEmerald)
                    )

                    grades.forEach { (name, interval, color) ->
                        Button(
                            onClick = { onGrade(name) },
                            modifier = Modifier.weight(1f).height(46.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (name == "Easy") NeonEmerald else SlateElevation,
                                contentColor = if (name == "Easy") ObsidianBg else TextHighEmphasis
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(name, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text(interval, fontFamily = JetBrainsMono, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
