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
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.FoodNutritionItem
import com.example.data.repository.HealthContentRepository
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSegmentedControl
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.getGlassBackground

@Composable
fun DietPlanScreen(
    language: String,
    onNavigateBack: () -> Unit
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    var selectedMealIndex by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val mealOptions = if (isBengali) {
        listOf("প্রাতঃরাশ", "দুপুরের আহার", "রাতের খাবার")
    } else {
        listOf("Breakfast", "Lunch", "Dinner")
    }

    val mealPlan = HealthContentRepository.mealPlans.getOrNull(selectedMealIndex)
        ?: HealthContentRepository.mealPlans.first()

    val filteredFoods = if (searchQuery.isBlank()) {
        HealthContentRepository.indianFoods
    } else {
        HealthContentRepository.indianFoods.filter {
            it.nameEn.contains(searchQuery, ignoreCase = true) ||
            it.nameBn.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true)
        }
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
            // Top Bar
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

                Column {
                    Text(
                        text = if (isBengali) "খাবার তালিকা ও পুষ্টি" else "Diet Plan & Nutrition",
                        color = primaryText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isBengali) "স্বাস্থ্যকর সুষম খাবারের নির্দেশনা" else "Balanced nutrition guidance",
                        color = secondaryText,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Meal Selector
            GlassSegmentedControl(
                options = mealOptions,
                selectedIndex = selectedMealIndex,
                onOptionSelected = { selectedMealIndex = it }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Featured Meal Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column {
                    // Meal Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali) mealPlan.titleBn else mealPlan.titleEn,
                            color = primaryText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x33F97316))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = FlameOrange,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${mealPlan.calories} kcal",
                                    color = FlameOrange,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val items = if (isBengali) mealPlan.itemsBn else mealPlan.itemsEn
                    items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "• ",
                                color = if (isDark) NeonCyan else Color(0xFF0284C7),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = item,
                                color = primaryText,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isBengali) mealPlan.notesBn else mealPlan.notesEn,
                        color = secondaryText,
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Indian Food Nutrition Search Section
            Text(
                text = if (isBengali) "ভারতীয় খাবারের পুষ্টি উপাদান" else "Indian Food Nutrition Library",
                color = primaryText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Search input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = if (isBengali) "খাবারের নাম দিয়ে খুঁজুন (চাল, রুটি, ডাল...)" else "Search food (Rice, Roti, Dal...)",
                        color = secondaryText,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (isDark) NeonCyan else Color(0xFF0284C7)
                    )
                },
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                    unfocusedBorderColor = if (isDark) Color(0x3338BDF8) else Color(0x3394A3B8),
                    focusedContainerColor = if (isDark) Color(0x330F172A) else Color(0xD9FFFFFF),
                    unfocusedContainerColor = if (isDark) Color(0x220F172A) else Color(0xB8FFFFFF),
                    focusedTextColor = primaryText,
                    unfocusedTextColor = primaryText
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Food Items List
            filteredFoods.forEach { food ->
                FoodNutritionCard(
                    food = food,
                    isBengali = isBengali,
                    isDark = isDark
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun FoodNutritionCard(
    food: FoodNutritionItem,
    isBengali: Boolean,
    isDark: Boolean
) {
    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isBengali) food.nameBn else food.nameEn,
                        color = primaryText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isBengali) food.servingSizeBn else food.servingSizeEn,
                        color = secondaryText,
                        fontSize = 12.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x3310B981))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${food.calories} kcal",
                        color = EmeraldGlow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Macro Nutrients row: Protein, Carbs, Fats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDark) Color(0x330F172A) else Color(0x1F94A3B8))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MacroPill(label = if (isBengali) "প্রোটিন" else "Protein", value = "${food.proteinGrams}g", color = if (isDark) NeonCyan else Color(0xFF0284C7), secondaryText = secondaryText)
                MacroPill(label = if (isBengali) "কার্বস" else "Carbs", value = "${food.carbsGrams}g", color = GoldenSun, secondaryText = secondaryText)
                MacroPill(label = if (isBengali) "ফ্যাট" else "Fat", value = "${food.fatGrams}g", color = FlameOrange, secondaryText = secondaryText)
                MacroPill(label = if (isBengali) "ফাইবার" else "Fiber", value = "${food.fiberGrams}g", color = EmeraldGlow, secondaryText = secondaryText)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isBengali) food.healthBenefitsBn else food.healthBenefitsEn,
                color = if (isDark) CyanAccent else Color(0xFF0284C7),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun MacroPill(label: String, value: String, color: Color, secondaryText: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = secondaryText, fontSize = 10.sp)
    }
}
