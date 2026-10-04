package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ExerciseGuideItem
import com.example.data.repository.HealthContentRepository
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
import kotlinx.coroutines.delay

@Composable
fun ExerciseGuideScreen(
    language: String,
    onNavigateBack: () -> Unit
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted
    var selectedCategoryIndex by remember { mutableIntStateOf(1) } // Default Walking
    val scrollState = rememberScrollState()

    var isWorkoutActive by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(isWorkoutActive) {
        if (isWorkoutActive) {
            while (isWorkoutActive) {
                delay(1000)
                elapsedSeconds++
            }
        }
    }

    val categories = if (isBengali) {
        listOf("প্রাথমিক", "হাঁটা", "স্ট্রেচিং")
    } else {
        listOf("Beginner", "Walking", "Stretching")
    }

    val currentCategoryKey = when (selectedCategoryIndex) {
        0 -> "Beginner"
        1 -> "Walking"
        else -> "Stretching"
    }

    val exercises = HealthContentRepository.exerciseGuides.filter {
        it.category.equals(currentCategoryKey, ignoreCase = true)
    }.ifEmpty { HealthContentRepository.exerciseGuides }

    val featuredExercise = exercises.first()

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
                    text = if (isBengali) "ব্যায়াম নির্দেশিকা" else "Exercise Guide",
                    color = primaryText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Category Segmented Tabs
            GlassSegmentedControl(
                options = categories,
                selectedIndex = selectedCategoryIndex,
                onOptionSelected = { selectedCategoryIndex = it }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Featured Exercise Card with Image
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column {
                    // Image Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.exercise_fitness_walk),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isBengali) featuredExercise.titleBn else featuredExercise.titleEn,
                        color = primaryText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Pills for duration and difficulty
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x3300F2FE))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${featuredExercise.durationMinutes} min",
                                    color = if (isDark) NeonCyan else Color(0xFF0284C7),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x3310B981))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBengali) featuredExercise.difficultyBn else featuredExercise.difficultyEn,
                                    color = if (isDark) EmeraldGlow else Color(0xFF059669),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isBengali) featuredExercise.descriptionBn else featuredExercise.descriptionEn,
                        color = secondaryText,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Instructions
                    val instructions = if (isBengali) featuredExercise.instructionsBn else featuredExercise.instructionsEn
                    instructions.forEachIndexed { idx, ins ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${idx + 1}. ",
                                color = if (isDark) NeonCyan else Color(0xFF0284C7),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = ins,
                                color = primaryText,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Active Workout Session Timer
                    if (isWorkoutActive) {
                        val mins = elapsedSeconds / 60
                        val secs = elapsedSeconds % 60
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isDark) Color(0x3310B981) else Color(0x2210B981))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isBengali) "চলমান সেশন: " else "Session in Progress: ",
                                    color = primaryText,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = String.format("%02d:%02d", mins, secs),
                                    color = if (isDark) EmeraldGlow else Color(0xFF059669),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Start/Stop Guide Button
                    GlassButton(
                        text = if (isWorkoutActive)
                            (if (isBengali) "সেশন শেষ করুন" else "End Session")
                        else
                            (if (isBengali) "গাইড শুরু করুন" else "Start Guide"),
                        trailingIcon = if (isWorkoutActive) Icons.Default.Stop else Icons.Default.PlayArrow,
                        onClick = {
                            if (isWorkoutActive) {
                                isWorkoutActive = false
                            } else {
                                elapsedSeconds = 0
                                isWorkoutActive = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
