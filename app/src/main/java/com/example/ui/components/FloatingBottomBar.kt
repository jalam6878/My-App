package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted

enum class BottomNavTab(
    val titleEn: String,
    val titleBn: String,
    val icon: ImageVector,
    val testTag: String
) {
    HOME("Home", "হোম", Icons.Default.Home, "bottom_nav_home"),
    ACTIVITY("Activity", "কার্যকলাপ", Icons.Default.BarChart, "bottom_nav_activity"),
    ACHIEVEMENTS("Awards", "অর্জন", Icons.Default.EmojiEvents, "bottom_nav_achievements"),
    HEALTH("Health", "স্বাস্থ্য", Icons.Default.Favorite, "bottom_nav_health"),
    PROFILE("Profile", "প্রোফাইল", Icons.Default.Person, "bottom_nav_profile")
}

@Composable
fun FloatingBottomBar(
    currentTab: BottomNavTab,
    isBengali: Boolean,
    onTabSelected: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val barBg = if (isDark) {
        listOf(Color(0xE60D172A), Color(0xF2070B14))
    } else {
        listOf(Color(0xF5FFFFFF), Color(0xEBF1F5F9))
    }

    val barBorder = if (isDark) {
        listOf(Color(0x6638BDF8), Color(0x221E293B))
    } else {
        listOf(Color(0x6638BDF8), Color(0x3394A3B8))
    }

    val activeColor = if (isDark) NeonCyan else Color(0xFF0284C7)
    val inactiveColor = if (isDark) TextMuted else TextDarkMuted

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(Brush.verticalGradient(barBg))
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(barBorder),
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    val label = if (isBengali) tab.titleBn else tab.titleEn

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                if (isSelected) {
                                    if (isDark) Color(0x3300F2FE) else Color(0x260284C7)
                                } else Color.Transparent
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = activeColor.copy(alpha = 0.3f)),
                                onClick = { onTabSelected(tab) }
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag(tab.testTag),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = label,
                                tint = if (isSelected) activeColor else inactiveColor,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = label,
                                color = if (isSelected) activeColor else inactiveColor,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}
