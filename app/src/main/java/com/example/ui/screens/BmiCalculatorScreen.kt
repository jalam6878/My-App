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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.ui.components.ActivityRing
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSegmentedControl
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.RoseHeart
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.getGlassBackground
import java.util.Locale

@Composable
fun BmiCalculatorScreen(
    currentHeightCm: Float,
    currentWeightKg: Float,
    language: String,
    onSaveHeightWeight: (Float, Float) -> Unit,
    onNavigateBack: () -> Unit
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    var selectedUnitIndex by remember { mutableIntStateOf(0) } // 0: Metric, 1: Imperial
    var heightCm by remember { mutableFloatStateOf(if (currentHeightCm > 0) currentHeightCm else 170f) }
    var weightKg by remember { mutableFloatStateOf(if (currentWeightKg > 0) currentWeightKg else 65f) }
    val scrollState = rememberScrollState()

    val units = listOf("Metric", "Imperial")

    // BMI calculation: weight in kg / (height in meters * height in meters)
    val heightMeters = (heightCm / 100f).coerceAtLeast(0.5f)
    val bmi = (weightKg / (heightMeters * heightMeters)).coerceIn(10f, 60f)

    val (bmiCategory, categoryColor, adviceTextEn, adviceTextBn) = when {
        bmi < 18.5f -> Quad(
            if (isBengali) "কম ওজন (Underweight)" else "Underweight",
            GoldenSun,
            "You are below standard weight. Nourish with wholesome nutrient-dense meals.",
            "আপনার ওজন মানদণ্ডের চেয়ে কম। সুষম ও পুষ্টিকর খাবার গ্রহণ করুন।"
        )
        bmi in 18.5f..24.9f -> Quad(
            if (isBengali) "স্বাভাবিক (Normal)" else "Normal",
            EmeraldGlow,
            "You are in a healthy weight range. Maintain your active lifestyle!",
            "আপনার ওজন স্বাভাবিক ও স্বাস্থ্যকর সীমার মধ্যে আছে। ধারাবাহিকতা বজায় রাখুন!"
        )
        bmi in 25.0f..29.9f -> Quad(
            if (isBengali) "অতিরিক্ত ওজন (Overweight)" else "Overweight",
            FlameOrange,
            "Slightly above normal. Consistent daily walking and dietary portion balance can help.",
            "স্বাভাবিকের চেয়ে সামান্য বেশি। নিয়মিত হাঁটা ও পরিমিত খাবার সহায়ক হবে।"
        )
        else -> Quad(
            if (isBengali) "স্থূলতা (Obese)" else "Obese",
            RoseHeart,
            "BMI indicates elevated range. Focus on gentle daily steps and consult a healthcare professional.",
            "উচ্চ ঝুঁকি নির্দেশক। প্রতিদিন ধীরেসুস্থে হাঁটা শুরু করুন ও চিকিৎসকের পরামর্শ নিন।"
        )
    }

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
                    text = if (isBengali) "BMI ক্যালকুলেটর" else "BMI Calculator",
                    color = primaryText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Units Segmented Control
            GlassSegmentedControl(
                options = units,
                selectedIndex = selectedUnitIndex,
                onOptionSelected = { selectedUnitIndex = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Height Adjuster Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali) "উচ্চতা (Height)" else "Height",
                            color = primaryText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (selectedUnitIndex == 0)
                                "${heightCm.toInt()} cm"
                            else
                                "${(heightCm / 30.48f).toInt()} ft ${((heightCm % 30.48f) / 2.54f).toInt()} in",
                            color = if (isDark) NeonCyan else Color(0xFF0284C7),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = heightCm,
                        onValueChange = {
                            heightCm = it
                            onSaveHeightWeight(heightCm, weightKg)
                        },
                        valueRange = 120f..220f,
                        colors = SliderDefaults.colors(
                            thumbColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                            activeTrackColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                            inactiveTrackColor = if (isDark) Color(0x3338BDF8) else Color(0x33CBD5E1)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Weight Adjuster Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali) "ওজন (Weight)" else "Weight",
                            color = primaryText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (selectedUnitIndex == 0)
                                String.format(Locale.US, "%.1f kg", weightKg)
                            else
                                String.format(Locale.US, "%.1f lbs", weightKg * 2.20462f),
                            color = EmeraldGlow,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = weightKg,
                        onValueChange = {
                            weightKg = it
                            onSaveHeightWeight(heightCm, weightKg)
                        },
                        valueRange = 35f..160f,
                        colors = SliderDefaults.colors(
                            thumbColor = EmeraldGlow,
                            activeTrackColor = EmeraldGlow,
                            inactiveTrackColor = if (isDark) Color(0x3310B981) else Color(0x33A7F3D0)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Circular BMI Gauge
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ActivityRing(
                    currentSteps = ((bmi - 10f) * 200).toInt(),
                    goalSteps = 10000,
                    ringSize = 210.dp,
                    strokeWidth = 16.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isBengali) "আপনার BMI" else "Your BMI",
                            color = secondaryText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = String.format(Locale.US, "%.1f", bmi),
                            color = primaryText,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = bmiCategory,
                            color = categoryColor,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Healthy Range Capsule
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isDark) Color(0x331E293B) else Color(0x22CBD5E1))
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isBengali) "স্বাস্থ্যকর পরিসর: ১৮.৫ - ২৪.৯" else "Healthy Range: 18.5 – 24.9",
                    color = primaryText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Advice Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                borderColor = categoryColor.copy(alpha = 0.4f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = categoryColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isBengali) adviceTextBn else adviceTextEn,
                        color = primaryText,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Disclaimer Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isDark) Color(0x1F334155) else Color(0x1994A3B8))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = secondaryText,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isBengali)
                        "বিএমআই প্রাপ্তবয়স্কদের সাধারণ স্ক্রিনিং নির্দেশক মাত্র, এটি কোনো চূড়ান্ত রোগ নির্ণয় নয়।"
                    else
                        "BMI is an adult screening estimate only and is not a comprehensive medical diagnosis.",
                    color = secondaryText,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
