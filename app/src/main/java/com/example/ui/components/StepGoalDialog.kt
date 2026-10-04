package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun StepGoalDialog(
    currentGoal: Int,
    isBengali: Boolean,
    onDismiss: () -> Unit,
    onGoalSelected: (Int) -> Unit
) {
    val predefinedGoals = listOf(3000, 5000, 6000, 8000, 10000, 12000, 15000)
    var isCustomSelected by remember { mutableStateOf(currentGoal !in predefinedGoals) }
    var customGoalText by remember { mutableStateOf(currentGoal.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0F172A),
        title = {
            Text(
                text = if (isBengali) "দৈনিক পদক্ষেপের লক্ষ্য নির্ধারণ" else "Set Daily Step Goal",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                predefinedGoals.chunked(3).forEach { rowList ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowList.forEach { g ->
                            val isSelected = !isCustomSelected && currentGoal == g
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) Color(0x3300F2FE) else Color(0x221E293B))
                                    .border(
                                        1.dp,
                                        if (isSelected) NeonCyan else Color(0x2238BDF8),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable {
                                        isCustomSelected = false
                                        onGoalSelected(g)
                                        onDismiss()
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = String.format("%,d", g),
                                    color = if (isSelected) NeonCyan else TextWhite,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Custom Goal
                OutlinedTextField(
                    value = customGoalText,
                    onValueChange = {
                        customGoalText = it
                        isCustomSelected = true
                    },
                    label = { Text(if (isBengali) "কাস্টম লক্ষ্য" else "Custom Goal", color = TextMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = Color(0x3338BDF8)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val g = customGoalText.toIntOrNull() ?: currentGoal
                    onGoalSelected(g.coerceIn(1000, 100000))
                    onDismiss()
                }
            ) {
                Text(if (isBengali) "নিশ্চিত" else "Apply", color = NeonCyan, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isBengali) "বাতিল" else "Cancel", color = TextMuted)
            }
        }
    )
}
