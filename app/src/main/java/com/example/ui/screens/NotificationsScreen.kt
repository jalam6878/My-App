package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.RoseHeart
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.getGlassBackground

@Composable
fun NotificationsScreen(
    language: String,
    notifMorning: Boolean,
    notifWater: Boolean,
    notifWalk: Boolean,
    notifGoal: Boolean,
    notifEvening: Boolean,
    notifSleep: Boolean,
    onToggleMorning: (Boolean) -> Unit,
    onToggleWater: (Boolean) -> Unit,
    onToggleWalk: (Boolean) -> Unit,
    onToggleGoal: (Boolean) -> Unit,
    onToggleEvening: (Boolean) -> Unit,
    onToggleSleep: (Boolean) -> Unit,
    onTestNotification: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted
    val scrollState = rememberScrollState()

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
                    text = if (isBengali) "বিজ্ঞপ্তি সেটিংস" else "Notifications",
                    color = primaryText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Notification Toggles List
            NotificationItemRow(
                icon = Icons.Default.WbSunny,
                title = if (isBengali) "সকালের স্বাস্থ্য টিপ" else "Morning Health Tip",
                timeTag = "7:00 AM",
                accentColor = GoldenSun,
                checked = notifMorning,
                onCheckedChange = onToggleMorning
            )

            Spacer(modifier = Modifier.height(10.dp))

            NotificationItemRow(
                icon = Icons.Default.WaterDrop,
                title = if (isBengali) "পানি পানের তাগিদ" else "Water Reminder",
                timeTag = "10:00 AM",
                accentColor = ElectricBlue,
                checked = notifWater,
                onCheckedChange = onToggleWater
            )

            Spacer(modifier = Modifier.height(10.dp))

            NotificationItemRow(
                icon = Icons.Default.DirectionsWalk,
                title = if (isBengali) "হাঁটার অনুস্মারক" else "Walking Reminder",
                timeTag = "4:00 PM",
                accentColor = NeonCyan,
                checked = notifWalk,
                onCheckedChange = onToggleWalk
            )

            Spacer(modifier = Modifier.height(10.dp))

            NotificationItemRow(
                icon = Icons.Default.EmojiEvents,
                title = if (isBengali) "লক্ষ্য ও অর্জন" else "Goal Achievement",
                timeTag = "Instant",
                accentColor = FlameOrange,
                checked = notifGoal,
                onCheckedChange = onToggleGoal
            )

            Spacer(modifier = Modifier.height(10.dp))

            NotificationItemRow(
                icon = Icons.Default.WbTwilight,
                title = if (isBengali) "সন্ধ্যার সুস্থতা" else "Evening Wellness",
                timeTag = "7:00 PM",
                accentColor = RoseHeart,
                checked = notifEvening,
                onCheckedChange = onToggleEvening
            )

            Spacer(modifier = Modifier.height(10.dp))

            NotificationItemRow(
                icon = Icons.Default.Bedtime,
                title = if (isBengali) "ঘুমের সময়সূচি" else "Sleep Reminder",
                timeTag = "10:00 PM",
                accentColor = NeonPurple,
                checked = notifSleep,
                onCheckedChange = onToggleSleep
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Test Notification Trigger
            GlassButton(
                text = if (isBengali) "বিজ্ঞপ্তি পরীক্ষা করুন (Test)" else "Test Local Notification Now",
                trailingIcon = Icons.Default.NotificationsActive,
                onClick = onTestNotification,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun NotificationItemRow(
    icon: ImageVector,
    title: String,
    timeTag: String,
    accentColor: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        color = primaryText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = timeTag,
                        color = secondaryText,
                        fontSize = 12.sp
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = if (isDark) Color(0xFF070B14) else Color.White,
                    checkedTrackColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                    uncheckedThumbColor = secondaryText,
                    uncheckedTrackColor = if (isDark) Color(0x33334155) else Color(0x33CBD5E1)
                )
            )
        }
    }
}
