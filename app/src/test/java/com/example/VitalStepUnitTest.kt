package com.example

import com.example.data.repository.HealthContentRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VitalStepUnitTest {

    @Test
    fun testBmiCalculation() {
        val heightMeters = 1.70f
        val weightKg = 65f
        val bmi = weightKg / (heightMeters * heightMeters)
        assertEquals(22.5f, bmi, 0.1f)
    }

    @Test
    fun testStepDistanceAndCalorieCalculation() {
        val steps = 6248
        val distanceKm = (steps * 0.72f) / 1000f
        val calories = (steps * 0.042f).toInt()
        val activeMinutes = steps / 105

        assertEquals(4.5f, distanceKm, 0.1f)
        assertEquals(262, calories)
        assertEquals(59, activeMinutes)
    }

    @Test
    fun testHealthContentAvailability() {
        val routines = HealthContentRepository.dailyRoutines
        val tips = HealthContentRepository.healthTips
        val foods = HealthContentRepository.indianFoods
        val exercises = HealthContentRepository.exerciseGuides
        val quotes = HealthContentRepository.motivationQuotes
        val achievements = HealthContentRepository.defaultAchievements

        assertTrue("Routines should not be empty", routines.isNotEmpty())
        assertTrue("Tips should not be empty", tips.isNotEmpty())
        assertTrue("Indian foods should not be empty", foods.isNotEmpty())
        assertTrue("Exercises should not be empty", exercises.isNotEmpty())
        assertTrue("Quotes should not be empty", quotes.isNotEmpty())
        assertTrue("Achievements should not be empty", achievements.isNotEmpty())
    }

    @Test
    fun testBilingualContentIntegrity() {
        HealthContentRepository.healthTips.forEach { tip ->
            assertTrue("English tip description required", tip.descriptionEn.isNotBlank())
            assertTrue("Bengali tip description required", tip.descriptionBn.isNotBlank())
        }
        HealthContentRepository.indianFoods.forEach { food ->
            assertTrue("English food name required", food.nameEn.isNotBlank())
            assertTrue("Bengali food name required", food.nameBn.isNotBlank())
            assertTrue("Calories must be positive", food.calories > 0)
        }
    }
}
