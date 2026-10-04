package com.example.ui.screens

import android.content.Context
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Straighten
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DailyActivityEntity
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSegmentedControl
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.LocalIsDarkTheme
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
fun WellnessReportScreen(
    activities: List<DailyActivityEntity>,
    language: String,
    onExportCsv: (Context) -> Unit,
    onNavigateBack: () -> Unit
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    val context = LocalContext.current
    var selectedPeriodIndex by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    val periodOptions = if (isBengali) {
        listOf("সাপ্তাহিক", "মাসিক", "বার্ষিক")
    } else {
        listOf("Weekly", "Monthly", "Yearly")
    }

    val totalSteps = activities.sumOf { it.steps }
    val avgSteps = if (activities.isNotEmpty()) totalSteps / activities.size else 0
    val totalDistance = activities.sumOf { it.distanceKm.toDouble() }.toFloat()
    val totalCalories = activities.sumOf { it.calories }

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
                    text = if (isBengali) "ওয়েলনেস রিপোর্ট" else "Wellness Report",
                    color = primaryText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Timeframe Segmented Control
            GlassSegmentedControl(
                options = periodOptions,
                selectedIndex = selectedPeriodIndex,
                onOptionSelected = { selectedPeriodIndex = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date Range Display
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
                    text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date()),
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

            // Step Progression Chart
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column {
                    Text(
                        text = if (isBengali) "কদমের গতিপ্রকৃতি" else "Step Progression",
                        color = primaryText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val maxSteps = activities.maxOfOrNull { it.steps }?.coerceAtLeast(1000) ?: 10000
                    val daysLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        daysLabels.forEachIndexed { index, d ->
                            val activityForDay = activities.getOrNull(index)
                            val frac = if (activityForDay != null && maxSteps > 0) {
                                (activityForDay.steps.toFloat() / maxSteps.toFloat()).coerceIn(0.08f, 1f)
                            } else {
                                0.08f
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(20.dp)
                                        .fillMaxHeight(frac)
                                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                if (isDark) listOf(NeonCyan, ElectricBlue)
                                                else listOf(Color(0xFF0284C7), Color(0xFF38BDF8))
                                            )
                                        )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = d,
                                    color = secondaryText,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4 Report Metric Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ReportMetricItem(
                    icon = Icons.Default.DirectionsWalk,
                    title = if (isBengali) "মোট পদক্ষেপ" else "Total Steps",
                    value = String.format("%,d", totalSteps),
                    accentColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )

                ReportMetricItem(
                    icon = Icons.Default.Speed,
                    title = if (isBengali) "গড় পদক্ষেপ" else "Avg Steps",
                    value = String.format("%,d", avgSteps),
                    accentColor = EmeraldGlow,
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ReportMetricItem(
                    icon = Icons.Default.Straighten,
                    title = if (isBengali) "মোট দূরত্ব" else "Distance",
                    value = String.format(Locale.US, "%.1f km", totalDistance),
                    accentColor = ElectricBlue,
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )

                ReportMetricItem(
                    icon = Icons.Default.LocalFireDepartment,
                    title = if (isBengali) "মোট ক্যালরি" else "Calories",
                    value = "$totalCalories kcal",
                    accentColor = FlameOrange,
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Export Data Button
            GlassButton(
                text = if (isBengali) "CSV হিসেবে তথ্য এক্সপোর্ট করুন" else "Export Activity Data (CSV)",
                trailingIcon = Icons.Default.FileDownload,
                onClick = { onExportCsv(context) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ReportMetricItem(
    icon: ImageVector,
    title: String,
    value: String,
    accentColor: Color,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted
    val bg = if (isDark) Color(0x331E293B) else Color(0xD9FFFFFF)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(bg)
            .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                    color = secondaryText,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                color = primaryText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
