package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "vitalstep_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val LANGUAGE = stringPreferencesKey("language") // "en" or "bn"
        val THEME_MODE = stringPreferencesKey("theme_mode") // "dark", "light"
        val DAILY_STEP_GOAL = intPreferencesKey("daily_step_goal")
        val DAILY_WATER_GOAL = intPreferencesKey("daily_water_goal")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_AGE = intPreferencesKey("user_age")
        val USER_HEIGHT_CM = floatPreferencesKey("user_height_cm")
        val USER_WEIGHT_KG = floatPreferencesKey("user_weight_kg")
        val TARGET_WEIGHT_KG = floatPreferencesKey("target_weight_kg")
        val IS_GOOGLE_SIGNED_IN = booleanPreferencesKey("is_google_signed_in")
        val LAST_BACKUP_TIME = longPreferencesKey("last_backup_time")

        // Notification toggles
        val NOTIF_MORNING_TIP = booleanPreferencesKey("notif_morning_tip")
        val NOTIF_WATER_REMINDER = booleanPreferencesKey("notif_water_reminder")
        val NOTIF_WALKING_REMINDER = booleanPreferencesKey("notif_walking_reminder")
        val NOTIF_GOAL_ACHIEVEMENT = booleanPreferencesKey("notif_goal_achievement")
        val NOTIF_EVENING_WELLNESS = booleanPreferencesKey("notif_evening_wellness")
        val NOTIF_SLEEP_REMINDER = booleanPreferencesKey("notif_sleep_reminder")
        val AUTO_CHECK_UPDATES = booleanPreferencesKey("auto_check_updates")
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map { it[ONBOARDING_COMPLETED] ?: false }
    val language: Flow<String> = context.dataStore.data.map { it[LANGUAGE] ?: "en" }
    val themeMode: Flow<String> = context.dataStore.data.map { it[THEME_MODE] ?: "dark" }
    val dailyStepGoal: Flow<Int> = context.dataStore.data.map { it[DAILY_STEP_GOAL] ?: 10000 }
    val dailyWaterGoal: Flow<Int> = context.dataStore.data.map { it[DAILY_WATER_GOAL] ?: 2500 }
    val userName: Flow<String> = context.dataStore.data.map { it[USER_NAME] ?: "Rahul Das" }
    val userEmail: Flow<String> = context.dataStore.data.map { it[USER_EMAIL] ?: "rahul@example.com" }
    val userAge: Flow<Int> = context.dataStore.data.map { it[USER_AGE] ?: 28 }
    val userHeightCm: Flow<Float> = context.dataStore.data.map { it[USER_HEIGHT_CM] ?: 170f }
    val userWeightKg: Flow<Float> = context.dataStore.data.map { it[USER_WEIGHT_KG] ?: 65f }
    val targetWeightKg: Flow<Float> = context.dataStore.data.map { it[TARGET_WEIGHT_KG] ?: 60f }
    val isGoogleSignedIn: Flow<Boolean> = context.dataStore.data.map { it[IS_GOOGLE_SIGNED_IN] ?: false }
    val lastBackupTime: Flow<Long> = context.dataStore.data.map { it[LAST_BACKUP_TIME] ?: 0L }

    val notifMorningTip: Flow<Boolean> = context.dataStore.data.map { it[NOTIF_MORNING_TIP] ?: true }
    val notifWaterReminder: Flow<Boolean> = context.dataStore.data.map { it[NOTIF_WATER_REMINDER] ?: true }
    val notifWalkingReminder: Flow<Boolean> = context.dataStore.data.map { it[NOTIF_WALKING_REMINDER] ?: true }
    val notifGoalAchievement: Flow<Boolean> = context.dataStore.data.map { it[NOTIF_GOAL_ACHIEVEMENT] ?: true }
    val notifEveningWellness: Flow<Boolean> = context.dataStore.data.map { it[NOTIF_EVENING_WELLNESS] ?: true }
    val notifSleepReminder: Flow<Boolean> = context.dataStore.data.map { it[NOTIF_SLEEP_REMINDER] ?: true }
    val autoCheckUpdates: Flow<Boolean> = context.dataStore.data.map { it[AUTO_CHECK_UPDATES] ?: true }

    suspend fun setAutoCheckUpdates(enabled: Boolean) {
        context.dataStore.edit { it[AUTO_CHECK_UPDATES] = enabled }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { it[ONBOARDING_COMPLETED] = completed }
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { it[LANGUAGE] = lang }
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { it[THEME_MODE] = mode }
    }

    suspend fun setDailyStepGoal(goal: Int) {
        context.dataStore.edit { it[DAILY_STEP_GOAL] = goal }
    }

    suspend fun setDailyWaterGoal(goal: Int) {
        context.dataStore.edit { it[DAILY_WATER_GOAL] = goal }
    }

    suspend fun updateUserProfile(
        name: String,
        email: String,
        age: Int,
        heightCm: Float,
        weightKg: Float,
        targetKg: Float
    ) {
        context.dataStore.edit {
            it[USER_NAME] = name
            it[USER_EMAIL] = email
            it[USER_AGE] = age
            it[USER_HEIGHT_CM] = heightCm
            it[USER_WEIGHT_KG] = weightKg
            it[TARGET_WEIGHT_KG] = targetKg
        }
    }

    suspend fun setGoogleSignedIn(signedIn: Boolean, email: String = "", name: String = "") {
        context.dataStore.edit {
            it[IS_GOOGLE_SIGNED_IN] = signedIn
            if (signedIn && email.isNotEmpty()) {
                it[USER_EMAIL] = email
            }
            if (signedIn && name.isNotEmpty()) {
                it[USER_NAME] = name
            }
        }
    }

    suspend fun updateLastBackupTime(timestamp: Long = System.currentTimeMillis()) {
        context.dataStore.edit { it[LAST_BACKUP_TIME] = timestamp }
    }

    suspend fun updateNotification(key: Preferences.Key<Boolean>, enabled: Boolean) {
        context.dataStore.edit { it[key] = enabled }
    }

    suspend fun clearAllData() {
        context.dataStore.edit { it.clear() }
    }
}
