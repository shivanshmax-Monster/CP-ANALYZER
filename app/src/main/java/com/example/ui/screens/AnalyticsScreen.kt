package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.AlgoUiState

@Composable
fun AnalyticsScreen(
    uiState: AlgoUiState,
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
        // Readiness Hero Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = DeepZincCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "INTERVIEW READINESS",
                            color = NeonEmerald,
                            fontFamily = JetBrainsMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "98.4%",
                            color = TextHighEmphasis,
                            fontFamily = JetBrainsMono,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Calculated across 648 verified algorithmic solves",
                            color = TextMediumEmphasis,
                            fontSize = 11.sp
                        )
                    }

                    // Circular ring gauge
                    Box(modifier = Modifier.size(64.dp), contentAlignment = Alignment.Center) {
                        Canvas(modifier = Modifier.size(60.dp)) {
                            val stroke = 6.dp.toPx()
                            drawArc(
                                color = SlateElevation,
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(stroke)
                            )
                            drawArc(
                                color = ElectricIndigo,
                                startAngle = -90f,
                                sweepAngle = 350f,
                                useCenter = false,
                                style = Stroke(stroke, cap = StrokeCap.Round)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = LightIndigo,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                // 3 quick badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatBadge("Recall Accuracy", "94.2%", NeonEmerald, Modifier.weight(1f))
                    StatBadge("Avg Rehearsal", "14.5m", LightIndigo, Modifier.weight(1f))
                    StatBadge("Ebbinghaus Decay", "-2.1%", AmberGold, Modifier.weight(1f))
                }
            }
        }

        // Memory Retention Curve Graph Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = DeepZincCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ShowChart,
                            contentDescription = null,
                            tint = NeonEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Retention Curve Stabilization",
                            color = TextHighEmphasis,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Text(
                        text = "SM-2 Ideal vs Actual",
                        color = TextMediumEmphasis,
                        fontFamily = JetBrainsMono,
                        fontSize = 10.sp
                    )
                }

                // Custom Canvas Curve
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Horizontal grid lines
                        for (i in 1..3) {
                            val y = h * (i / 4f)
                            drawLine(
                                color = SlateBorder,
                                start = Offset(0f, y),
                                end = Offset(w, y),
                                strokeWidth = 1.dp.toPx()
                            )
                        }

                        // Actual retention curve (smooth line)
                        val path = Path()
                        val points = listOf(
                            Offset(0f, h * 0.1f),
                            Offset(w * 0.2f, h * 0.35f),
                            Offset(w * 0.22f, h * 0.15f), // reset review 1
                            Offset(w * 0.45f, h * 0.32f),
                            Offset(w * 0.47f, h * 0.12f), // reset review 2
                            Offset(w * 0.75f, h * 0.22f),
                            Offset(w * 0.77f, h * 0.08f), // reset review 3
                            Offset(w, h * 0.14f)
                        )

                        path.moveTo(points[0].x, points[0].y)
                        for (i in 1 until points.size) {
                            path.lineTo(points[i].x, points[i].y)
                        }

                        drawPath(
                            path = path,
                            color = NeonEmerald,
                            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Day 1 (Learn)", color = TextMediumEmphasis, fontSize = 10.sp)
                    Text("Day 7", color = TextMediumEmphasis, fontSize = 10.sp)
                    Text("Day 21", color = TextMediumEmphasis, fontSize = 10.sp)
                    Text("Day 45 (Mastered)", color = NeonEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Topic Mastery Breakdown
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = DeepZincCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Topic Recall Strengths & Deficits",
                    color = TextHighEmphasis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                val topics = listOf(
                    Triple("Dynamic Programming", 38, CrimsonRed),
                    Triple("Graphs & Topological Sort", 85, NeonEmerald),
                    Triple("Binary Trees & BST", 92, NeonEmerald),
                    Triple("Two Pointers & Sliding Window", 96, NeonEmerald),
                    Triple("Bit Manipulation", 64, AmberGold)
                )

                topics.forEach { (topic, pct, color) ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = topic, color = TextHighEmphasis, fontSize = 12.sp)
                            Text(
                                text = "$pct%",
                                color = color,
                                fontFamily = JetBrainsMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                        LinearProgressIndicator(
                            progress = { pct / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = color,
                            trackColor = SlateElevation
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun StatBadge(
    title: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = SlateElevation.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(0.8.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = title, color = TextMediumEmphasis, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                color = valueColor,
                fontFamily = JetBrainsMono,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}
