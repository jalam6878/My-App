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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.DarkGlassBackgroundGradient
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.getGlassBackground

@Composable
fun ProfileScreen(
    userName: String,
    userEmail: String,
    stepGoal: Int,
    language: String,
    isGoogleSignedIn: Boolean,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onToggleLanguage: () -> Unit,
    onStepGoalClicked: () -> Unit
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    val scrollState = rememberScrollState()

    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

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
                    text = if (isBengali) "প্রোফাইল" else "Profile",
                    color = primaryText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isDark) Color(0x331E293B) else Color(0x1F94A3B8))
                        .clickable(onClick = onNavigateToSettings),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = primaryText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // User Info Card with Avatar
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(if (isDark) Color(0x3300F2FE) else Color(0x260284C7))
                        .border(2.dp, if (isDark) NeonCyan else Color(0xFF0284C7), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = if (isDark) NeonCyan else Color(0xFF0284C7),
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = userName,
                    color = primaryText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = userEmail,
                    color = secondaryText,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Profile Options Menu List
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ProfileMenuItem(
                    icon = Icons.Default.Person,
                    title = if (isBengali) "আমার প্রোফাইল" else "My Profile",
                    subtitle = if (isBengali) "বয়স, উচ্চতা, ওজন সম্পাদনা" else "Edit Personal Details",
                    accentColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                    isDark = isDark,
                    onClick = onNavigateToEditProfile
                )

                ProfileMenuItem(
                    icon = Icons.Default.CloudDone,
                    title = if (isBengali) "গুগল অ্যাকাউন্ট ও ব্যাকআপ" else "Google Account & Backup",
                    subtitle = if (isGoogleSignedIn)
                        (if (isBengali) "সংযুক্ত রয়েছে" else "Connected")
                    else
                        (if (isBengali) "ঐচ্ছিক ক্লাউড সিঙ্ক" else "Optional Cloud Sync"),
                    accentColor = EmeraldGlow,
                    isDark = isDark,
                    onClick = onNavigateToSettings
                )

                ProfileMenuItem(
                    icon = Icons.Default.Assessment,
                    title = if (isBengali) "ওয়েলনেস রিপোর্ট" else "Wellness Report",
                    subtitle = if (isBengali) "সাপ্তাহিক ও মাসিক সারাংশ" else "Weekly & Monthly Report Card",
                    accentColor = NeonPurple,
                    isDark = isDark,
                    onClick = onNavigateToReports
                )

                ProfileMenuItem(
                    icon = Icons.Default.Language,
                    title = if (isBengali) "ভাষা পরিবর্তন" else "Language",
                    subtitle = if (isBengali) "বাংলা (সক্রিয়)" else "English (Active)",
                    accentColor = GoldenSun,
                    isDark = isDark,
                    onClick = onToggleLanguage
                )

                ProfileMenuItem(
                    icon = Icons.Default.DirectionsWalk,
                    title = if (isBengali) "দৈনিক পদক্ষেপ লক্ষ্য" else "Daily Step Goal",
                    subtitle = String.format("%,d steps", stepGoal),
                    accentColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                    isDark = isDark,
                    onClick = onStepGoalClicked
                )

                ProfileMenuItem(
                    icon = Icons.Default.Tune,
                    title = if (isBengali) "স্বাস্থ্য সেটিংস ও বিজ্ঞপ্তি" else "Notifications & Preferences",
                    subtitle = if (isBengali) "পানি ও হাঁটার অনুস্মারক" else "Water, Walk & Goal Reminders",
                    accentColor = CyanAccent,
                    isDark = isDark,
                    onClick = onNavigateToNotifications
                )
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = primaryText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = secondaryText,
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = secondaryText,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
