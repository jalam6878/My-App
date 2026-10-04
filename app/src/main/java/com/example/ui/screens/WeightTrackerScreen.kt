package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.WeightEntity
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.getGlassBackground
import java.util.Locale

@Composable
fun WeightTrackerScreen(
    currentWeightKg: Float,
    targetWeightKg: Float,
    weightHistory: List<WeightEntity>,
    language: String,
    onRecordWeight: (Float, String) -> Unit,
    onDeleteWeight: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted
    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }
    var inputWeightText by remember { mutableStateOf(String.format(Locale.US, "%.1f", currentWeightKg)) }
    var inputNotes by remember { mutableStateOf("") }

    val diffToTarget = currentWeightKg - targetWeightKg

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getGlassBackground(isDark))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .padding(bottom = 90.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isDark) Color(0x331E293B) else Color(0x22CBD5E1))
                        .clickable(onClick = onNavigateBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = primaryText,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = if (isBengali) "ওজন ট্র্যাকার" else "Weight Tracker",
                    color = primaryText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Current Weight Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isBengali) "বর্তমান ওজন" else "Current Weight",
                            color = secondaryText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = String.format(Locale.US, "%.1f", currentWeightKg),
                                color = primaryText,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "kg",
                                color = if (isDark) NeonCyan else Color(0xFF0284C7),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isBengali) "লক্ষ্য: %.1f kg".format(targetWeightKg) else "Target: %.1f kg".format(targetWeightKg),
                            color = primaryText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x3310B981))
                            .border(1.dp, EmeraldGlow.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingDown,
                                contentDescription = null,
                                tint = EmeraldGlow,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (diffToTarget > 0)
                                    "-${String.format(Locale.US, "%.1f", diffToTarget)} kg to go"
                                else
                                    "Target Reached!",
                                color = EmeraldGlow,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Trend Chart Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column {
                    Text(
                        text = if (isBengali) "ওজন পরিবর্তনের চিত্র" else "Weight Trend",
                        color = primaryText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Line Chart Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val points = listOf(
                                Offset(w * 0.1f, h * 0.35f),
                                Offset(w * 0.35f, h * 0.45f),
                                Offset(w * 0.65f, h * 0.60f),
                                Offset(w * 0.9f, h * 0.75f)
                            )

                            // Grid lines
                            drawLine(
                                color = if (isDark) Color(0x2238BDF8) else Color(0x330284C7),
                                start = Offset(0f, h * 0.25f),
                                end = Offset(w, h * 0.25f),
                                strokeWidth = 1.dp.toPx()
                            )
                            drawLine(
                                color = if (isDark) Color(0x2238BDF8) else Color(0x330284C7),
                                start = Offset(0f, h * 0.75f),
                                end = Offset(w, h * 0.75f),
                                strokeWidth = 1.dp.toPx()
                            )

                            // Trend curve
                            val path = Path()
                            path.moveTo(points[0].x, points[0].y)
                            for (i in 1 until points.size) {
                                val prev = points[i - 1]
                                val curr = points[i]
                                val midX = (prev.x + curr.x) / 2
                                path.cubicTo(midX, prev.y, midX, curr.y, curr.x, curr.y)
                            }

                            drawPath(
                                path = path,
                                brush = Brush.horizontalGradient(
                                    if (isDark) listOf(ElectricBlue, NeonCyan)
                                    else listOf(Color(0xFF0284C7), Color(0xFF0EA5E9))
                                ),
                                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                            )

                            // Circles at points
                            points.forEach { pt ->
                                drawCircle(
                                    color = if (isDark) NeonCyan else Color(0xFF0284C7),
                                    radius = 5.dp.toPx(),
                                    center = pt
                                )
                                drawCircle(
                                    color = if (isDark) Color(0xFF070B14) else Color.White,
                                    radius = 2.5.dp.toPx(),
                                    center = pt
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("Jul", "Aug", "Sep", "Oct").forEach { month ->
                            Text(
                                text = month,
                                color = secondaryText,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Record Weight Button
            GlassButton(
                text = if (isBengali) "ওজন রেকর্ড করুন" else "Record Weight",
                trailingIcon = Icons.Default.MonitorWeight,
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Logs
            Text(
                text = if (isBengali) "সাম্প্রতিক রেকর্ড" else "Weight History",
                color = primaryText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            weightHistory.forEach { item ->
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${item.weightKg} kg",
                                color = primaryText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${item.date} ${if (item.notes.isNotEmpty()) "• ${item.notes}" else ""}",
                                color = secondaryText,
                                fontSize = 12.sp
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = secondaryText,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onDeleteWeight(item.id) }
                        )
                    }
                }
            }
        }

        // Record Weight Modal Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                containerColor = if (isDark) Color(0xFF0F172A) else Color(0xFFF8FAFC),
                title = {
                    Text(
                        text = if (isBengali) "নতুন ওজন রেকর্ড করুন" else "Record New Weight",
                        color = primaryText,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = inputWeightText,
                            onValueChange = { inputWeightText = it },
                            label = { Text("Weight (kg)", color = secondaryText) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = primaryText,
                                unfocusedTextColor = primaryText,
                                focusedBorderColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                                unfocusedBorderColor = if (isDark) Color(0x3338BDF8) else Color(0x3394A3B8)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = inputNotes,
                            onValueChange = { inputNotes = it },
                            label = { Text("Notes (optional)", color = secondaryText) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = primaryText,
                                unfocusedTextColor = primaryText,
                                focusedBorderColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                                unfocusedBorderColor = if (isDark) Color(0x3338BDF8) else Color(0x3394A3B8)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val w = inputWeightText.toFloatOrNull() ?: currentWeightKg
                            onRecordWeight(w, inputNotes)
                            showDialog = false
                        }
                    ) {
                        Text(text = "Save", color = if (isDark) NeonCyan else Color(0xFF0284C7), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(text = "Cancel", color = secondaryText)
                    }
                }
            )
        }
    }
}
