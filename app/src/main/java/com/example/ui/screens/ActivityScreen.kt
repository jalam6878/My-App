package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DailyActivityEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSegmentedControl
import com.example.ui.theme.DarkGlassBackgroundGradient
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.getGlassBackground
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityScreen(
    currentSteps: Int,
    distanceKm: Float,
    calories: Int,
    activeMinutes: Int,
    stepGoal: Int,
    recentActivities: List<DailyActivityEntity>,
    language: String,
    onNavigateBack: () -> Unit
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    var selectedTimeframe by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    val timeframeOptions = if (isBengali) {
        listOf("দিন", "সপ্তাহ", "মাস", "বছর")
    } else {
        listOf("Day", "Week", "Month", "Year")
    }

    val dateFormatted = SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(Date())

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBengali) "কার্যকলাপ" else "Activity",
                    color = primaryText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.Default.DirectionsWalk,
                    contentDescription = null,
                    tint = if (isDark) NeonCyan else Color(0xFF0284C7),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Timeframe Segmented Control
            GlassSegmentedControl(
                options = timeframeOptions,
                selectedIndex = selectedTimeframe,
                onOptionSelected = { selectedTimeframe = it }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Date Navigator Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isDark) Color(0x2E1E293B) else Color(0x1F94A3B8))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous",
                    tint = secondaryText,
                    modifier = Modifier.size(22.dp)
                )

                Text(
                    text = dateFormatted,
                    color = primaryText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next",
                    tint = secondaryText,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Step Bar Chart
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali) "কদম বিশ্লেষণ" else "Step Analytics",
                            color = primaryText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (isBengali) "লক্ষ্য %,d".format(stepGoal) else "Goal %,d".format(stepGoal),
                            color = if (isDark) NeonCyan else Color(0xFF0284C7),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bar Chart
                    val bars = listOf(
                        "6am" to 0.15f,
                        "9am" to 0.45f,
                        "12pm" to 0.70f,
                        "3pm" to 0.85f,
                        "6pm" to 0.60f,
                        "9pm" to 0.30f
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(horizontal = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        bars.forEach { (label, fraction) ->
                            val animHeight by animateFloatAsState(
                                targetValue = fraction,
                                label = "barHeight"
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(22.dp)
                                        .fillMaxHeight(animHeight)
                                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(NeonCyan, ElectricBlue)
                                            )
                                        )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = label,
                                    color = secondaryText,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Daily Summary Title
            Text(
                text = if (isBengali) "দৈনিক সারসংক্ষেপ" else "Daily Summary",
                color = primaryText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Summary Metrics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActivitySummaryItem(
                    icon = Icons.Default.DirectionsWalk,
                    value = String.format("%,d", currentSteps),
                    label = if (isBengali) "পদক্ষেপ" else "Steps",
                    accentColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )

                ActivitySummaryItem(
                    icon = Icons.Default.Straighten,
                    value = String.format(Locale.US, "%.1f km", distanceKm),
                    label = if (isBengali) "দূরত্ব" else "Distance",
                    accentColor = ElectricBlue,
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActivitySummaryItem(
                    icon = Icons.Default.LocalFireDepartment,
                    value = "$calories kcal",
                    label = if (isBengali) "ক্যালরি" else "Calories",
                    accentColor = FlameOrange,
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )

                ActivitySummaryItem(
                    icon = Icons.Default.Timer,
                    value = "$activeMinutes min",
                    label = if (isBengali) "সক্রিয় সময়" else "Active Time",
                    accentColor = EmeraldGlow,
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Weekly Progress
            Text(
                text = if (isBengali) "সাপ্তাহিক অগ্রগতি" else "Weekly Progress",
                color = primaryText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                val days = listOf("M", "T", "W", "T", "F", "S", "S")
                val completed = listOf(true, true, true, false, true, false, false)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    days.forEachIndexed { index, day ->
                        val isDone = completed[index]
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isDone) Color(0x3310B981) else (if (isDark) Color(0x221E293B) else Color(0x1F94A3B8)))
                                    .border(
                                        width = 1.dp,
                                        color = if (isDone) EmeraldGlow else secondaryText.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Completed",
                                        tint = EmeraldGlow,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(secondaryText.copy(alpha = 0.5f))
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = day,
                                color = if (isDone) primaryText else secondaryText,
                                fontSize = 12.sp,
                                fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActivitySummaryItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    accentColor: Color,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val bg = if (isDark) Color(0x331E293B) else Color(0xD9FFFFFF)
    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(bg)
            .border(
                1.dp,
                if (isDark) accentColor.copy(alpha = 0.35f) else Color(0x3394A3B8),
                RoundedCornerShape(18.dp)
            )
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = value,
                    color = primaryText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    color = secondaryText,
                    fontSize = 11.sp
                )
            }
        }
    }
}
