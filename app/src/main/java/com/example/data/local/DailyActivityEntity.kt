package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_activity")
data class DailyActivityEntity(
    @PrimaryKey
    val date: String, // YYYY-MM-DD
    val steps: Int = 0,
    val distanceKm: Float = 0f,
    val calories: Int = 0,
    val activeMinutes: Int = 0,
    val goalSteps: Int = 10000,
    val isGoalCompleted: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)
