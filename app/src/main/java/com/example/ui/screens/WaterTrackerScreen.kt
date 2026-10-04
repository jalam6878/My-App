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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ActivityRing
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
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
fun WaterTrackerScreen(
    currentWaterMl: Int,
    targetWaterMl: Int,
    language: String,
    onAddWater: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    val scrollState = rememberScrollState()

    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    val currentLiters = currentWaterMl / 1000f
    val targetLiters = targetWaterMl / 1000f
    val progressFraction = (currentWaterMl.toFloat() / targetWaterMl.toFloat()).coerceIn(0f, 1f)
    val progressPercent = (progressFraction * 100).toInt()

    val animProgress by animateFloatAsState(targetValue = progressFraction, label = "waterProgress")

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
                        .background(if (isDark) Color(0x331E293B) else Color(0x1F94A3B8))
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
                    text = if (isBengali) "পানি ট্র্যাকার" else "Water Tracker",
                    color = primaryText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Circular Water Gauge with +/- Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Minus 250ml
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isDark) Color(0x331E293B) else Color(0xD9FFFFFF))
                        .border(1.dp, if (isDark) Color(0x3338BDF8) else Color(0x3394A3B8), CircleShape)
                        .clickable { onAddWater(-250) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Minus",
                        tint = primaryText,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(18.dp))

                // Circular Ring
                ActivityRing(
                    currentSteps = currentWaterMl,
                    goalSteps = targetWaterMl,
                    ringSize = 210.dp,
                    strokeWidth = 16.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(if (isDark) Color(0x3300F2FE) else Color(0x260284C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = if (isDark) NeonCyan else Color(0xFF0284C7),
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = String.format(Locale.US, "%.1f L", currentLiters),
                            color = primaryText,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = if (isBengali) "লক্ষ্য: %.1f L".format(targetLiters) else "of %.1f L".format(targetLiters),
                            color = secondaryText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.width(18.dp))

                // Plus 250ml
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(ElectricBlue, NeonCyan))
                        )
                        .clickable { onAddWater(250) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Plus",
                        tint = Color(0xFF070B14),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Quick Add Cups Row (5 cups of 250ml)
            Text(
                text = if (isBengali) "এক ক্লিকে পানি যোগ করুন" else "Quick Add Cups (250 ml)",
                color = primaryText,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(5) { index ->
                    val isDrunk = currentWaterMl >= (index + 1) * 250
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isDrunk) {
                                    if (isDark) Color(0x3300F2FE) else Color(0x260284C7)
                                } else {
                                    if (isDark) Color(0x221E293B) else Color(0xD9FFFFFF)
                                }
                            )
                            .border(
                                1.dp,
                                if (isDrunk) (if (isDark) NeonCyan else Color(0xFF0284C7)) else (if (isDark) Color(0x2238BDF8) else Color(0x3394A3B8)),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onAddWater(250) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.LocalDrink,
                                contentDescription = null,
                                tint = if (isDrunk) (if (isDark) NeonCyan else Color(0xFF0284C7)) else secondaryText,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "250 ml",
                                color = if (isDrunk) primaryText else secondaryText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Today's Progress Bar Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isBengali) "আজকের অগ্রগতি" else "Today's Progress",
                            color = primaryText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "$progressPercent%",
                            color = if (isDark) NeonCyan else Color(0xFF0284C7),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { animProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = if (isDark) NeonCyan else Color(0xFF0284C7),
                        trackColor = if (isDark) Color(0x2238BDF8) else Color(0x2294A3B8),
                        strokeCap = StrokeCap.Round
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Health Tip on Hydration
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                backgroundColor = if (isDark) Color(0x1F1E293B) else Color(0xC4FFFFFF)
            ) {
                Text(
                    text = if (isBengali)
                        "নিয়মিত পানি পান কিডনির কার্যকারিতা বজায় রাখে এবং ত্বকের সজীবতা বৃদ্ধি করে। যাদের চিকিৎসকের কোনো ফ্লুইড সীমাবদ্ধতা আছে, তারা নির্দেশিত মাত্রা মেনে চলুন।"
                    else
                        "Consistent hydration supports cellular metabolism and kidney filtration. If you have medical fluid restrictions, adhere to your doctor's recommendations.",
                    color = secondaryText,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
