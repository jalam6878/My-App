package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AchievementEntity
import com.example.sensor.WalkingSessionState
import com.example.ui.components.ActivityRing
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassMetricCapsule
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GoldenSun
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

data class SessionSummaryData(
    val steps: Int,
    val distanceKm: Float,
    val calories: Int,
    val timeFormatted: String
)

@Composable
fun QuickActionChip(
    icon: ImageVector,
    title: String,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val primaryText = if (isDark) TextWhite else TextDark
    GlassCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = primaryText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun HomeScreen(
    currentSteps: Int,
    distanceKm: Float,
    calories: Int,
    activeMinutes: Int,
    stepGoal: Int,
    language: String,
    latestAchievement: AchievementEntity?,
    isSensorAvailable: Boolean,
    walkingSessionState: WalkingSessionState,
    sessionSteps: Int,
    sessionDistanceKm: Float,
    sessionCalories: Int,
    sessionDurationSeconds: Int,
    onStartWalkingSession: () -> Unit,
    onPauseWalkingSession: () -> Unit,
    onResumeWalkingSession: () -> Unit,
    onFinishWalkingSession: () -> Unit,
    onManualStepIncrement: (Int) -> Unit,
    onNavigateToActivity: () -> Unit,
    onNavigateToAchievements: () -> Unit,
    onNavigateToWater: () -> Unit,
    onNavigateToBmi: () -> Unit,
    onNavigateToMotivation: () -> Unit
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    val scrollState = rememberScrollState()

    var showFinishedSessionDialog by remember { mutableStateOf(false) }
    var lastFinishedSummary by remember { mutableStateOf<SessionSummaryData?>(null) }

    val dateFormatted = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(Date())
    val completionPercent = if (stepGoal > 0) ((currentSteps.toFloat() / stepGoal) * 100).toInt().coerceAtLeast(0) else 0

    val primaryTextColor = if (isDark) TextWhite else TextDark
    val secondaryTextColor = if (isDark) TextMuted else TextDarkMuted

    val sessionMins = sessionDurationSeconds / 60
    val sessionSecs = sessionDurationSeconds % 60
    val sessionTimeFormatted = String.format("%02d:%02d", sessionMins, sessionSecs)

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
            // Top Bar: Greeting & Sensor Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0x33F59E0B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = GoldenSun,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isBengali) "শুভ সকাল!" else "Good Morning!",
                            color = primaryTextColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isBengali) "আজ, $dateFormatted" else dateFormatted,
                            color = secondaryTextColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Real Hardware Sensor Status Badge (No fake step buttons)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isDark) Color(0x2210B981) else Color(0x1910B981))
                        .border(
                            1.dp,
                            if (isDark) EmeraldGlow.copy(alpha = 0.4f) else Color(0x4010B981),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isSensorAvailable) EmeraldGlow else GoldenSun)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isSensorAvailable) {
                                if (isBengali) "সেন্সর সক্রিয়" else "Sensor Active"
                            } else {
                                if (isBengali) "পেডোমিটার" else "Pedometer"
                            },
                            color = if (isSensorAvailable) EmeraldGlow else GoldenSun,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Main Circular Activity Ring - Displays Real Daily Steps
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToActivity() },
                contentAlignment = Alignment.Center
            ) {
                ActivityRing(
                    currentSteps = currentSteps,
                    goalSteps = stepGoal,
                    ringSize = 230.dp,
                    strokeWidth = 18.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isDark) Color(0x3300F2FE) else Color(0x260284C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsWalk,
                                contentDescription = null,
                                tint = if (isDark) NeonCyan else Color(0xFF0284C7),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isBengali) "আজকের মোট পদক্ষেপ" else "TODAY'S DAILY STEPS",
                            color = secondaryTextColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = String.format("%,d", currentSteps),
                            color = primaryTextColor,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )

                        Text(
                            text = if (isBengali) "/ %,d লক্ষ্য".format(stepGoal) else "/ %,d goal".format(stepGoal),
                            color = secondaryTextColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDark) Color(0x3310B981) else Color(0x2210B981))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isBengali) "$completionPercent% সম্পন্ন" else "$completionPercent% completed",
                                color = EmeraldGlow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // WALKING SESSION CONTROLLER (Prominent Real Step Tracking)
            // ==========================================
            WalkingSessionCard(
                state = walkingSessionState,
                dailySteps = currentSteps,
                sessionSteps = sessionSteps,
                sessionDistanceKm = sessionDistanceKm,
                sessionCalories = sessionCalories,
                sessionTimeFormatted = sessionTimeFormatted,
                isBengali = isBengali,
                isDark = isDark,
                onStart = onStartWalkingSession,
                onPause = onPauseWalkingSession,
                onResume = onResumeWalkingSession,
                onFinish = {
                    lastFinishedSummary = SessionSummaryData(
                        steps = sessionSteps,
                        distanceKm = sessionDistanceKm,
                        calories = sessionCalories,
                        timeFormatted = sessionTimeFormatted
                    )
                    onFinishWalkingSession()
                    showFinishedSessionDialog = true
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 3 Glass Metric Capsules (Daily Totals)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GlassMetricCapsule(
                    icon = Icons.Default.Straighten,
                    value = String.format(Locale.US, "%.1f km", distanceKm),
                    label = if (isBengali) "দৈনিক দূরত্ব" else "Distance",
                    accentColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                    modifier = Modifier.weight(1f)
                )

                GlassMetricCapsule(
                    icon = Icons.Default.LocalFireDepartment,
                    value = "$calories kcal",
                    label = if (isBengali) "দৈনিক ক্যালরি" else "Calories",
                    accentColor = FlameOrange,
                    modifier = Modifier.weight(1f)
                )

                GlassMetricCapsule(
                    icon = Icons.Default.Timer,
                    value = "$activeMinutes min",
                    label = if (isBengali) "সক্রিয় সময়" else "Active Time",
                    accentColor = EmeraldGlow,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Current Achievement Card
            val badgeTitle = if (isBengali) (latestAchievement?.titleBn ?: "শক্তির নতুন সূচনা") else (latestAchievement?.titleEn ?: "Energy Booster")
            val badgeDesc = if (isBengali) (latestAchievement?.descBn ?: "পদক্ষেপ নেওয়ার মাধ্যমে নতুন ব্যাজ আনলক করুন!") else (latestAchievement?.descEn ?: "Step forward to unlock achievements!")

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("current_achievement_card"),
                shape = RoundedCornerShape(22.dp),
                borderColor = GoldenSun.copy(alpha = 0.4f),
                onClick = onNavigateToAchievements
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(GoldenSun.copy(alpha = 0.4f), Color(0x15F59E0B))
                                )
                            )
                            .border(1.5.dp, GoldenSun, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = GoldenSun,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBengali) "বর্তমান অর্জন" else "Current Achievement",
                            color = GoldenSun,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = badgeTitle,
                            color = primaryTextColor,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = badgeDesc,
                            color = secondaryTextColor,
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = GoldenSun,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Daily Motivation Card
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("motivation_card"),
                shape = RoundedCornerShape(20.dp),
                borderColor = if (isDark) NeonCyan.copy(alpha = 0.25f) else Color(0x330284C7),
                onClick = onNavigateToMotivation
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✨",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBengali)
                                "আজ একটু হাঁটেন, আগামীকাল আরো সুস্থ থাকুন!"
                            else
                                "Small steps today, a healthier tomorrow.",
                            color = primaryTextColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isBengali)
                                "প্রতিটি পদক্ষেপ আপনাকে লক্ষ্যের দিকে এগিয়ে নিয়ে যায়।"
                            else
                                "Consistency always defeats intensity.",
                            color = if (isDark) CyanAccent else Color(0xFF0284C7),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionChip(
                    icon = Icons.Default.WaterDrop,
                    title = if (isBengali) "পানি ট্র্যাকার" else "Water",
                    accentColor = ElectricBlue,
                    onClick = onNavigateToWater,
                    modifier = Modifier.weight(1f)
                )

                QuickActionChip(
                    icon = Icons.Default.MonitorWeight,
                    title = if (isBengali) "BMI ক্যালকুলেটর" else "BMI Check",
                    accentColor = MintGreen,
                    onClick = onNavigateToBmi,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        val summary = lastFinishedSummary
        if (showFinishedSessionDialog && summary != null) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showFinishedSessionDialog = false },
                containerColor = if (isDark) Color(0xFF0F172A) else Color(0xFFF8FAFC),
                icon = {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0x3310B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = EmeraldGlow,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                title = {
                    Text(
                        text = if (isBengali) "হাঁটার সেশন সম্পন্ন!" else "Walking Session Completed!",
                        color = primaryTextColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isBengali) "অসাধারণ প্রচেষ্টা! আপনার সেশনের বিবরণ:" else "Great job! Here is your walking session summary:",
                            color = secondaryTextColor,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isDark) Color(0x331E293B) else Color(0x22CBD5E1))
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = String.format("%,d", summary.steps),
                                    color = if (isDark) NeonCyan else Color(0xFF0284C7),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isBengali) "সেশন কদম" else "Steps",
                                    color = secondaryTextColor,
                                    fontSize = 11.sp
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = summary.timeFormatted,
                                    color = primaryTextColor,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isBengali) "সময়" else "Duration",
                                    color = secondaryTextColor,
                                    fontSize = 11.sp
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${String.format(Locale.US, "%.2f", summary.distanceKm)} km",
                                    color = primaryTextColor,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isBengali) "দূরত্ব" else "Distance",
                                    color = secondaryTextColor,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (isBengali)
                                "✓ সেশনের কদম আপনার আজকের মোট পদক্ষেপে যোগ করা হয়েছে এবং Room ডেটাবেসে স্থায়ীভাবে সংরক্ষিত হয়েছে।"
                            else
                                "✓ Session steps are added to today's total and permanently saved in Room DB.",
                            color = EmeraldGlow,
                            fontSize = 11.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                },
                confirmButton = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Brush.horizontalGradient(listOf(ElectricBlue, NeonCyan)))
                            .clickable { showFinishedSessionDialog = false }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isBengali) "ধন্যবাদ ও চালিয়ে যান" else "Continue",
                            color = Color(0xFF070B14),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun WalkingSessionCard(
    state: WalkingSessionState,
    dailySteps: Int,
    sessionSteps: Int,
    sessionDistanceKm: Float,
    sessionCalories: Int,
    sessionTimeFormatted: String,
    isBengali: Boolean,
    isDark: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onFinish: () -> Unit
) {
    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("walking_session_card"),
        shape = RoundedCornerShape(24.dp),
        borderColor = when (state) {
            WalkingSessionState.WALKING -> EmeraldGlow.copy(alpha = 0.7f)
            WalkingSessionState.PAUSED -> GoldenSun.copy(alpha = 0.7f)
            WalkingSessionState.IDLE -> if (isDark) NeonCyan.copy(alpha = 0.45f) else Color(0x600284C7)
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            when (state) {
                WalkingSessionState.IDLE -> {
                    // Ready to start walking - Very prominent Start button
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isDark) Color(0x3300F2FE) else Color(0x220284C7)
                                    )
                                    .border(
                                        1.dp,
                                        if (isDark) NeonCyan else Color(0xFF0284C7),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsWalk,
                                    contentDescription = null,
                                    tint = if (isDark) NeonCyan else Color(0xFF0284C7),
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isBengali) "হাঁটার সেশন শুরু করুন" else "Start Walking Session",
                                    color = primaryText,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isBengali) "Android-এর প্রকৃত Step Sensor ব্যবহার করে কদম রেকর্ড হবে" else "Count real-time steps with Android hardware pedometer",
                                    color = secondaryText,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Large prominent Start Walking button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(26.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        if (isDark) listOf(ElectricBlue, NeonCyan)
                                        else listOf(Color(0xFF0284C7), Color(0xFF0EA5E9))
                                    )
                                )
                                .clickable { onStart() }
                                .padding(vertical = 14.dp)
                                .testTag("start_walking_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color(0xFF070B14),
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBengali) "🚶‍♂️ হাঁটা শুরু করুন (Start Walking)" else "🚶‍♂️ Start Walking",
                                    color = Color(0xFF070B14),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                WalkingSessionState.WALKING, WalkingSessionState.PAUSED -> {
                    val isWalking = state == WalkingSessionState.WALKING

                    // Status Badge with pulsing indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val dotColor = if (isWalking) EmeraldGlow else GoldenSun
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(dotColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isWalking) {
                                    if (isBengali) "হাঁটার সেশন চলছে (Active)" else "Walking Session Active"
                                } else {
                                    if (isBengali) "সেশন সাময়িক বিরতি (Paused)" else "Session Paused"
                                },
                                color = if (isWalking) EmeraldGlow else GoldenSun,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Elapsed Timer
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDark) Color(0x331E293B) else Color(0x2294A3B8))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = if (isDark) NeonCyan else Color(0xFF0284C7),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = sessionTimeFormatted,
                                color = primaryText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Separate Side-by-Side: Daily Steps vs Session Steps (Strict Requirement 1)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Card 1: Today's Daily Steps
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isDark) Color(0x331E293B) else Color(0x22CBD5E1))
                                .border(
                                    1.dp,
                                    if (isDark) Color(0x3360A5FA) else Color(0x330284C7),
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = if (isBengali) "দৈনিক মোট পদক্ষেপ" else "Daily Total",
                                    color = secondaryText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = String.format("%,d", dailySteps),
                                    color = primaryText,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Card 2: Walking Session Count
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isDark) Color(0x3300F2FE) else Color(0x260284C7)
                                )
                                .border(
                                    1.dp,
                                    if (isDark) NeonCyan.copy(alpha = 0.6f) else Color(0xFF0284C7),
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = if (isBengali) "সেশনের পদক্ষেপ" else "Session Steps",
                                    color = if (isDark) NeonCyan else Color(0xFF0284C7),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = String.format("%,d", sessionSteps),
                                    color = if (isDark) NeonCyan else Color(0xFF0284C7),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Secondary Session Metrics: Distance & Calories
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isDark) Color(0x220F172A) else Color(0x1F94A3B8))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Straighten,
                                contentDescription = null,
                                tint = if (isDark) CyanAccent else Color(0xFF0284C7),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${String.format(Locale.US, "%.2f", sessionDistanceKm)} km",
                                color = primaryText,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = FlameOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$sessionCalories kcal",
                                color = FlameOrange,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Session Action Buttons: Pause/Resume and Finish
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Pause / Resume Button
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(24.dp))
                                .background(
                                    if (isWalking) Color(0x33F59E0B) else Color(0x3310B981)
                                )
                                .border(
                                    1.dp,
                                    if (isWalking) GoldenSun else EmeraldGlow,
                                    RoundedCornerShape(24.dp)
                                )
                                .clickable {
                                    if (isWalking) onPause() else onResume()
                                }
                                .padding(vertical = 12.dp)
                                .testTag("pause_resume_session_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isWalking) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = if (isWalking) GoldenSun else EmeraldGlow,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isWalking) {
                                        if (isBengali) "বিরতি (Pause)" else "Pause"
                                    } else {
                                        if (isBengali) "চালু (Resume)" else "Resume"
                                    },
                                    color = if (isWalking) GoldenSun else EmeraldGlow,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Finish Button
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(24.dp))
                                .background(
                                    Brush.horizontalGradient(listOf(ElectricBlue, NeonCyan))
                                )
                                .clickable { onFinish() }
                                .padding(vertical = 12.dp)
                                .testTag("finish_session_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Stop,
                                    contentDescription = null,
                                    tint = Color(0xFF070B14),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isBengali) "সমাপ্ত (Finish)" else "Finish",
                                    color = Color(0xFF070B14),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
