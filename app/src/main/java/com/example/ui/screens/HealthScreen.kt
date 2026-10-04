package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WbSunny
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.repository.HealthContentRepository
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldGlow
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
fun HealthScreen(
    language: String,
    onNavigateToDietPlan: () -> Unit,
    onNavigateToExerciseGuide: () -> Unit,
    onNavigateToBmi: () -> Unit,
    onNavigateToWater: () -> Unit
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    var selectedChipIndex by remember { mutableIntStateOf(0) }
    var isLiked by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }

    val chips = if (isBengali) {
        listOf("আজকের", "সকালের", "খাবার", "ব্যায়াম")
    } else {
        listOf("Today", "Morning", "Food", "Exercise")
    }

    val currentRoutine = HealthContentRepository.dailyRoutines.firstOrNull {
        it.period == HealthContentRepository.getCurrentRoutinePeriod()
    } ?: HealthContentRepository.dailyRoutines.first()

    val featuredTip = HealthContentRepository.healthTips.first()

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
                    text = if (isBengali) "স্বাস্থ্য টিপস" else "Health Tips",
                    color = primaryText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null,
                    tint = EmeraldGlow,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Chips Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(chips) { index, chip ->
                    val isSelected = selectedChipIndex == index
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(ElectricBlue, NeonCyan))
                                else Brush.linearGradient(
                                    if (isDark) listOf(Color(0x331E293B), Color(0x331E293B))
                                    else listOf(Color(0x22CBD5E1), Color(0x22CBD5E1))
                                )
                            )
                            .border(
                                1.dp,
                                if (isSelected) NeonCyan else (if (isDark) Color(0x2238BDF8) else Color(0x330284C7)),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedChipIndex = index }
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = chip,
                            color = if (isSelected) Color(0xFF070B14) else primaryText,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Featured Health Card with Photo Banner
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column {
                    // Image Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.healthy_food_bowl),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = GoldenSun,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBengali) "আজকের টিপ" else "Today's Tip",
                            color = GoldenSun,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isBengali) featuredTip.descriptionBn else featuredTip.descriptionEn,
                        color = primaryText,
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Icons Row: Share, Like, Bookmark
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Share
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isDark) Color(0x331E293B) else Color(0x22CBD5E1))
                                    .clickable {
                                        val shareText = if (isBengali)
                                            "আজকের স্বাস্থ্য টিপ:\n${featuredTip.descriptionBn}\n\n- VitalStep"
                                        else
                                            "Today's Health Tip:\n${featuredTip.descriptionEn}\n\n- VitalStep"
                                        val intent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(Intent.EXTRA_TEXT, shareText)
                                        }
                                        context.startActivity(Intent.createChooser(intent, "Share Tip"))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = if (isDark) NeonCyan else Color(0xFF0284C7),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Like
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isDark) Color(0x331E293B) else Color(0x22CBD5E1))
                                    .clickable { isLiked = !isLiked },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Like",
                                    tint = if (isLiked) RoseHeart else primaryText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Bookmark
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isDark) Color(0x331E293B) else Color(0x22CBD5E1))
                                    .clickable { isSaved = !isSaved },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Save",
                                    tint = if (isSaved) GoldenSun else primaryText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Text(
                            text = if (isBengali) "২ মিনিট পড়া" else "2 min read",
                            color = secondaryText,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // More Categories
            Text(
                text = if (isBengali) "অন্যান্য বিভাগ" else "More Categories",
                color = primaryText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HealthCategoryCircle(
                    icon = Icons.Default.Restaurant,
                    label = if (isBengali) "পুষ্টি ও খাবার" else "Nutrition",
                    accentColor = RoseHeart,
                    isDark = isDark,
                    onClick = onNavigateToDietPlan,
                    modifier = Modifier.weight(1f)
                )

                HealthCategoryCircle(
                    icon = Icons.Default.DirectionsWalk,
                    label = if (isBengali) "হাঁটা ও ব্যায়াম" else "Walking",
                    accentColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                    isDark = isDark,
                    onClick = onNavigateToExerciseGuide,
                    modifier = Modifier.weight(1f)
                )

                HealthCategoryCircle(
                    icon = Icons.Default.Bedtime,
                    label = if (isBengali) "পরিমিত ঘুম" else "Sleep",
                    accentColor = NeonPurple,
                    isDark = isDark,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )

                HealthCategoryCircle(
                    icon = Icons.Default.Psychology,
                    label = if (isBengali) "মানসিক শান্তি" else "Mind",
                    accentColor = MintGreen,
                    isDark = isDark,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Current Time-of-Day Routine Card
            Text(
                text = if (isBengali) "বর্তমান সময়ের স্বাস্থ্যবিধি" else "Current Routine (${currentRoutine.timeRange})",
                color = primaryText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                borderColor = if (isDark) NeonCyan.copy(alpha = 0.35f) else Color(0x400284C7)
            ) {
                Column {
                    Text(
                        text = if (isBengali) currentRoutine.titleBn else currentRoutine.titleEn,
                        color = if (isDark) NeonCyan else Color(0xFF0284C7),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isBengali) currentRoutine.descBn else currentRoutine.descEn,
                        color = secondaryText,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val tips = if (isBengali) currentRoutine.tipsBn else currentRoutine.tipsEn
                    tips.forEach { tip ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "• ",
                                color = EmeraldGlow,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = tip,
                                color = primaryText,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HealthCategoryCircle(
    icon: ImageVector,
    label: String,
    accentColor: Color,
    isDark: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryText = if (isDark) TextWhite else TextDark
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(if (isDark) Color(0x331E293B) else Color(0x22CBD5E1))
                .border(1.dp, accentColor.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = accentColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = primaryText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}
