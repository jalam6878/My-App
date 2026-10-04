package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.example.data.local.AchievementEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSegmentedControl
import com.example.ui.theme.DarkGlassBackgroundGradient
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.getGlassBackground

@Composable
fun AchievementsScreen(
    achievements: List<AchievementEntity>,
    language: String,
    onAchievementClicked: (AchievementEntity) -> Unit = {}
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    val categories = if (isBengali) {
        listOf("পদক্ষেপ", "ধারাবাহিকতা", "বিশেষ")
    } else {
        listOf("Steps", "Streaks", "Special")
    }

    val categoryKeys = listOf("steps", "streaks", "special")
    val currentKey = categoryKeys[selectedCategoryIndex]
    val filteredList = achievements.filter { it.category == currentKey }

    // Find the highest unlocked achievement or default to 6K
    val featuredAchievement = achievements.firstOrNull { it.id == "step_6k" }
        ?: achievements.firstOrNull { it.isUnlocked }
        ?: achievements.firstOrNull()

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
                    text = if (isBengali) "অর্জন ও ব্যাজ" else "Achievements",
                    color = primaryText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = GoldenSun,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Segmented Category Tabs
            GlassSegmentedControl(
                options = categories,
                selectedIndex = selectedCategoryIndex,
                onOptionSelected = { selectedCategoryIndex = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Featured Hero Golden Medal Card
            if (featuredAchievement != null) {
                val fTitle = if (isBengali) featuredAchievement.titleBn else featuredAchievement.titleEn
                val fDesc = if (isBengali) featuredAchievement.descBn else featuredAchievement.descEn

                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    backgroundColor = if (isDark) Color(0x332B1E05) else Color(0xD9FFFBEB),
                    borderColor = GoldenSun.copy(alpha = 0.5f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Golden Medal Badge
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            GoldenSun,
                                            FlameOrange,
                                            Color(0xFF78350F)
                                        )
                                    )
                                )
                                .border(3.dp, Color(0xFFFDE68A), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(54.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = if (featuredAchievement.category == "steps")
                                String.format("%,d", featuredAchievement.targetValue)
                            else
                                "${featuredAchievement.targetValue} Days",
                            color = GoldenSun,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = fTitle,
                            color = primaryText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = fDesc,
                            color = secondaryText,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x3310B981))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isBengali) "সাফল্য অর্জিত হয়েছে!" else "Badge Unlocked!",
                                color = MintGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Achievement Gallery Grid
            Text(
                text = if (isBengali) "ব্যাজ গ্যালারি" else "Badge Gallery",
                color = primaryText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            val chunked = filteredList.chunked(2)
            chunked.forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { item ->
                        AchievementGridItem(
                            achievement = item,
                            isBengali = isBengali,
                            isDark = isDark,
                            onClick = { onAchievementClicked(item) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementGridItem(
    achievement: AchievementEntity,
    isBengali: Boolean,
    isDark: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isUnlocked = achievement.isUnlocked
    val title = if (isBengali) achievement.titleBn else achievement.titleEn
    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    val itemBg = if (isUnlocked) {
        if (isDark) Color(0x381E293B) else Color(0xD9FFFFFF)
    } else {
        if (isDark) Color(0x220F172A) else Color(0x1F94A3B8)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(itemBg)
            .border(
                width = 1.dp,
                color = if (isUnlocked) GoldenSun.copy(alpha = 0.5f) else (if (isDark) Color(0x2238BDF8) else Color(0x3394A3B8)),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) Color(0x33F59E0B) else (if (isDark) Color(0x1F334155) else Color(0x2294A3B8))
                    )
                    .border(
                        1.dp,
                        if (isUnlocked) GoldenSun else Color(0x3394A3B8),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = GoldenSun,
                        modifier = Modifier.size(26.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = secondaryText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (achievement.category == "steps")
                    "${achievement.targetValue / 1000}K"
                else
                    "${achievement.targetValue}",
                color = if (isUnlocked) GoldenSun else secondaryText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = title,
                color = if (isUnlocked) primaryText else secondaryText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
