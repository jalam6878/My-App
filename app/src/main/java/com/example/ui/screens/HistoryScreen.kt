package com.example.ui.screens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.History
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DailyActivityEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSegmentedControl
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
fun HistoryScreen(
    activities: List<DailyActivityEntity>,
    language: String,
    onNavigateBack: () -> Unit
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    var selectedViewMode by remember { mutableIntStateOf(0) } // 0: Calendar, 1: List
    val scrollState = rememberScrollState()

    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    val viewOptions = if (isBengali) listOf("ক্যালেন্ডার", "তালিকা") else listOf("Calendar", "List")

    val todayDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val selectedActivity = activities.firstOrNull { it.date == todayDateStr }
        ?: activities.firstOrNull()
        ?: DailyActivityEntity(
            date = todayDateStr,
            steps = 0,
            distanceKm = 0f,
            calories = 0,
            activeMinutes = 0,
            goalSteps = 10000,
            isGoalCompleted = false
        )

    val currentMonthYear = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())

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
                        .background(if (isDark) Color(0x331E293B) else Color(0x2294A3B8))
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
                    text = if (isBengali) "কার্যকলাপ ইতিহাস" else "Activity History",
                    color = primaryText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Segmented Control
            GlassSegmentedControl(
                options = viewOptions,
                selectedIndex = selectedViewMode,
                onOptionSelected = { selectedViewMode = it }
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (selectedViewMode == 0) {
                // Calendar View
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Column {
                        // Month Navigator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Prev Month",
                                tint = secondaryText,
                                modifier = Modifier.size(22.dp)
                            )

                            Text(
                                text = currentMonthYear,
                                color = primaryText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Next Month",
                                tint = secondaryText,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Day headers
                        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            daysOfWeek.forEach { d ->
                                Text(
                                    text = d,
                                    color = secondaryText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.width(36.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Real Calendar Grid matching activities
                        val activityDatesMap = activities.associateBy { it.date }
                        val todayDay = try {
                            SimpleDateFormat("d", Locale.getDefault()).format(Date()).toInt()
                        } catch (e: Exception) { 1 }

                        val sampleWeeks = listOf(
                            listOf(1, 2, 3, 4, 5, 6, 7),
                            listOf(8, 9, 10, 11, 12, 13, 14),
                            listOf(15, 16, 17, 18, 19, 20, 21),
                            listOf(22, 23, 24, 25, 26, 27, 28)
                        )

                        sampleWeeks.forEach { week ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                week.forEach { dayNum ->
                                    val isToday = dayNum == todayDay
                                    val hasRecordedWalk = isToday && selectedActivity.steps > 0

                                    val bg = when {
                                        isToday -> if (isDark) Color(0x3300F2FE) else Color(0x260284C7)
                                        hasRecordedWalk -> Color(0x2210B981)
                                        else -> Color.Transparent
                                    }

                                    val border = when {
                                        isToday -> if (isDark) NeonCyan else Color(0xFF0284C7)
                                        hasRecordedWalk -> MintGreen
                                        else -> if (isDark) Color(0x1AFFFFFF) else Color(0x1F94A3B8)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(bg)
                                            .border(1.dp, border, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$dayNum",
                                            color = if (isToday) (if (isDark) NeonCyan else Color(0xFF0284C7)) else primaryText,
                                            fontSize = 12.sp,
                                            fontWeight = if (isToday || hasRecordedWalk) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Legend
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LegendDot(color = secondaryText, label = if (isBengali) "হাঁটা হয়নি" else "No walk", secondaryText = secondaryText)
                            Spacer(modifier = Modifier.width(18.dp))
                            LegendDot(color = if (isDark) NeonCyan else Color(0xFF0284C7), label = if (isBengali) "আজকের দিন" else "Today", secondaryText = secondaryText)
                            Spacer(modifier = Modifier.width(18.dp))
                            LegendDot(color = MintGreen, label = if (isBengali) "লক্ষ্য অর্জিত" else "Goal Reached", secondaryText = secondaryText)
                        }
                    }
                }
            } else {
                // List View of past entries
                if (activities.isEmpty()) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = secondaryText,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (isBengali) "কোনো সংরক্ষিত ইতিহাস পাওয়া যায়নি" else "No activity history yet",
                                color = primaryText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isBengali) "হোম স্ক্রিনে হাঁটা শুরু করুন এবং আপনার কদম রেকর্ড হবে।" else "Start walking from the Home screen to record real steps.",
                                color = secondaryText,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        activities.take(14).forEach { act ->
                            GlassCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = act.date,
                                            color = primaryText,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${act.calories} kcal • ${String.format(Locale.US, "%.2f km", act.distanceKm)}",
                                            color = secondaryText,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Text(
                                        text = String.format("%,d steps", act.steps),
                                        color = if (act.isGoalCompleted) MintGreen else (if (isDark) NeonCyan else Color(0xFF0284C7)),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Selected Day Detail Card
            Text(
                text = if (isBengali) "আজকের কদমের বিবরণ" else "Today's Activity Details",
                color = primaryText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

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
                            text = selectedActivity.date,
                            color = if (isDark) NeonCyan else Color(0xFF0284C7),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = if (isDark) NeonCyan else Color(0xFF0284C7),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ActivitySummaryItem(
                            icon = Icons.Default.DirectionsWalk,
                            value = String.format("%,d", selectedActivity.steps),
                            label = if (isBengali) "কদম" else "Steps",
                            accentColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                            isDark = isDark,
                            modifier = Modifier.weight(1f)
                        )

                        ActivitySummaryItem(
                            icon = Icons.Default.Straighten,
                            value = String.format(Locale.US, "%.2f km", selectedActivity.distanceKm),
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
                            value = "${selectedActivity.calories} kcal",
                            label = if (isBengali) "ক্যালরি" else "Calories",
                            accentColor = FlameOrange,
                            isDark = isDark,
                            modifier = Modifier.weight(1f)
                        )

                        ActivitySummaryItem(
                            icon = Icons.Default.Timer,
                            value = "${selectedActivity.activeMinutes} min",
                            label = if (isBengali) "সক্রিয় সময়" else "Active Time",
                            accentColor = EmeraldGlow,
                            isDark = isDark,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LegendDot(color: Color, label: String, secondaryText: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, color = secondaryText, fontSize = 11.sp)
    }
}
