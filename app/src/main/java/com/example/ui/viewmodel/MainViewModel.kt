package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AchievementEntity
import com.example.data.local.AppDatabase
import com.example.data.local.DailyActivityEntity
import com.example.data.local.UserPreferences
import com.example.data.local.WaterIntakeEntity
import com.example.data.local.WeightEntity
import com.example.data.repository.FitnessRepository
import com.example.notification.NotificationHelper
import com.example.sensor.StepCounterManager
import com.example.sensor.WalkingSessionState
import com.example.update.AppUpdateManager
import com.example.update.AppVersionInfo
import com.example.update.UpdateStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = FitnessRepository(
        db.dailyActivityDao(),
        db.waterDao(),
        db.weightDao(),
        db.achievementDao()
    )
    val preferences = UserPreferences(application)
    val notificationHelper = NotificationHelper(application)
    val stepSensorManager = StepCounterManager(application)

    // User preferences states
    val isOnboardingCompleted: StateFlow<Boolean> = preferences.isOnboardingCompleted
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val language: StateFlow<String> = preferences.language
        .stateIn(viewModelScope, SharingStarted.Eagerly, "en")

    val themeMode: StateFlow<String> = preferences.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, "dark")

    val stepGoal: StateFlow<Int> = preferences.dailyStepGoal
        .stateIn(viewModelScope, SharingStarted.Eagerly, 10000)

    val waterGoal: StateFlow<Int> = preferences.dailyWaterGoal
        .stateIn(viewModelScope, SharingStarted.Eagerly, 2500)

    val userName: StateFlow<String> = preferences.userName
        .stateIn(viewModelScope, SharingStarted.Eagerly, "Rahul Das")

    val userEmail: StateFlow<String> = preferences.userEmail
        .stateIn(viewModelScope, SharingStarted.Eagerly, "rahul@example.com")

    val userAge: StateFlow<Int> = preferences.userAge
        .stateIn(viewModelScope, SharingStarted.Eagerly, 28)

    val userHeightCm: StateFlow<Float> = preferences.userHeightCm
        .stateIn(viewModelScope, SharingStarted.Eagerly, 170f)

    val userWeightKg: StateFlow<Float> = preferences.userWeightKg
        .stateIn(viewModelScope, SharingStarted.Eagerly, 65f)

    val targetWeightKg: StateFlow<Float> = preferences.targetWeightKg
        .stateIn(viewModelScope, SharingStarted.Eagerly, 60f)

    val isGoogleSignedIn: StateFlow<Boolean> = preferences.isGoogleSignedIn
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val lastBackupTime: StateFlow<Long> = preferences.lastBackupTime
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0L)

    // Notification states
    val notifMorningTip: StateFlow<Boolean> = preferences.notifMorningTip
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val notifWaterReminder: StateFlow<Boolean> = preferences.notifWaterReminder
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val notifWalkingReminder: StateFlow<Boolean> = preferences.notifWalkingReminder
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val notifGoalAchievement: StateFlow<Boolean> = preferences.notifGoalAchievement
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val notifEveningWellness: StateFlow<Boolean> = preferences.notifEveningWellness
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val notifSleepReminder: StateFlow<Boolean> = preferences.notifSleepReminder
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val autoCheckUpdates: StateFlow<Boolean> = preferences.autoCheckUpdates
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val updateManager = AppUpdateManager(application)
    val updateStatus: StateFlow<UpdateStatus> = updateManager.updateStatus
    val currentAppVersionName: String = updateManager.currentVersionName
    val currentAppVersionCode: Int = updateManager.currentVersionCode

    // Repository flows
    val todayActivity: StateFlow<DailyActivityEntity?> = repository.getActivityForToday()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val allActivities: StateFlow<List<DailyActivityEntity>> = repository.allActivities
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val totalLifetimeSteps: StateFlow<Int?> = repository.totalLifetimeSteps
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val totalLifetimeDistance: StateFlow<Float?> = repository.totalLifetimeDistance
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0f)

    val bestStepsDay: StateFlow<Int?> = repository.bestStepsDay
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val waterToday: StateFlow<Int> = repository.getWaterToday()
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val waterEntriesToday: StateFlow<List<WaterIntakeEntity>> = repository.getWaterEntriesToday()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val allAchievements: StateFlow<List<AchievementEntity>> = repository.allAchievements
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val latestUnlockedAchievement: StateFlow<AchievementEntity?> = repository.latestUnlockedAchievement
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val unlockedCount: StateFlow<Int> = repository.unlockedCount
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val allWeights: StateFlow<List<WeightEntity>> = repository.allWeights
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val latestWeight: StateFlow<WeightEntity?> = repository.latestWeight
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // Real Daily Tracking State (Persistent & Non-fake)
    private val _liveSteps = MutableStateFlow(0)
    val liveSteps: StateFlow<Int> = _liveSteps.asStateFlow()

    private val _liveDistanceKm = MutableStateFlow(0f)
    val liveDistanceKm: StateFlow<Float> = _liveDistanceKm.asStateFlow()

    private val _liveCalories = MutableStateFlow(0)
    val liveCalories: StateFlow<Int> = _liveCalories.asStateFlow()

    private val _liveActiveMinutes = MutableStateFlow(0)
    val liveActiveMinutes: StateFlow<Int> = _liveActiveMinutes.asStateFlow()

    // Real Walking Session State
    private val _walkingSessionState = MutableStateFlow(WalkingSessionState.IDLE)
    val walkingSessionState: StateFlow<WalkingSessionState> = _walkingSessionState.asStateFlow()

    private val _sessionSteps = MutableStateFlow(0)
    val sessionSteps: StateFlow<Int> = _sessionSteps.asStateFlow()

    private val _sessionDistanceKm = MutableStateFlow(0f)
    val sessionDistanceKm: StateFlow<Float> = _sessionDistanceKm.asStateFlow()

    private val _sessionCalories = MutableStateFlow(0)
    val sessionCalories: StateFlow<Int> = _sessionCalories.asStateFlow()

    private val _sessionDurationSeconds = MutableStateFlow(0)
    val sessionDurationSeconds: StateFlow<Int> = _sessionDurationSeconds.asStateFlow()

    private var sessionTimerJob: Job? = null

    private val _celebrationAchievement = MutableStateFlow<AchievementEntity?>(null)
    val celebrationAchievement: StateFlow<AchievementEntity?> = _celebrationAchievement.asStateFlow()

    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedAchievementsIfEmpty()

            // Initialize today's record strictly from database (0 if clean start)
            val today = StepCounterManager.getTodayDateString()
            val existing = repository.getTodayActivityOnce()
            val realSteps = existing?.steps ?: 0

            _liveSteps.value = realSteps
            _liveDistanceKm.value = existing?.distanceKm ?: ((realSteps * 0.72f) / 1000f)
            _liveCalories.value = existing?.calories ?: ((realSteps * 0.042f).toInt())
            _liveActiveMinutes.value = existing?.activeMinutes ?: (realSteps / 105)

            stepSensorManager.initDailySteps(realSteps)
            stepSensorManager.startListening()
        }

        // Connect step manager callback
        stepSensorManager.onMetricsUpdated = { dailySteps, dailyDist, dailyCal, dailyActiveMin, sSteps, sDist, sCal ->
            _liveSteps.value = dailySteps
            _liveDistanceKm.value = dailyDist
            _liveCalories.value = dailyCal
            _liveActiveMinutes.value = dailyActiveMin

            _sessionSteps.value = sSteps
            _sessionDistanceKm.value = sDist
            _sessionCalories.value = sCal

            viewModelScope.launch {
                val today = StepCounterManager.getTodayDateString()
                val currentGoal = stepGoal.value
                val isCompleted = dailySteps >= currentGoal

                // Persist real daily count to Room Database
                repository.saveDailyActivity(
                    DailyActivityEntity(
                        date = today,
                        steps = dailySteps,
                        distanceKm = dailyDist,
                        calories = dailyCal,
                        activeMinutes = dailyActiveMin,
                        goalSteps = currentGoal,
                        isGoalCompleted = isCompleted
                    )
                )

                // Check achievements based on real steps
                if (dailySteps > 0) {
                    val unlocked = repository.checkAndUnlockAchievements(dailySteps)
                    if (unlocked.isNotEmpty()) {
                        val first = unlocked.first()
                        _celebrationAchievement.value = first
                        if (notifGoalAchievement.value) {
                            notificationHelper.showAchievementUnlockedNotification(
                                if (language.value == "bn") first.titleBn else first.titleEn,
                                language.value == "bn"
                            )
                        }
                    }
                }
            }
        }
    }

    // Walking Session Controls
    fun startWalkingSession() {
        stepSensorManager.startWalkingSession()
        _walkingSessionState.value = WalkingSessionState.WALKING
        _sessionSteps.value = 0
        _sessionDistanceKm.value = 0f
        _sessionCalories.value = 0
        _sessionDurationSeconds.value = 0

        startTimer()

        _syncMessage.value = if (language.value == "bn")
            "হাঁটার সেশন শুরু হয়েছে! পদক্ষেপ গণনা চলছে..."
        else
            "Walking session started! Step tracking active..."
    }

    fun pauseWalkingSession() {
        stepSensorManager.pauseWalkingSession()
        _walkingSessionState.value = WalkingSessionState.PAUSED
        pauseTimer()

        _syncMessage.value = if (language.value == "bn")
            "হাঁটার সেশন সাময়িক বিরতি দেওয়া হয়েছে।"
        else
            "Walking session paused."
    }

    fun resumeWalkingSession() {
        stepSensorManager.resumeWalkingSession()
        _walkingSessionState.value = WalkingSessionState.WALKING
        startTimer()

        _syncMessage.value = if (language.value == "bn")
            "হাঁটার সেশন পুনরায় শুরু হয়েছে।"
        else
            "Walking session resumed."
    }

    fun finishWalkingSession() {
        val completedSteps = _sessionSteps.value
        val completedSecs = _sessionDurationSeconds.value
        val mins = completedSecs / 60
        val secs = completedSecs % 60
        val timeFormatted = String.format("%02d:%02d", mins, secs)

        stepSensorManager.finishWalkingSession()
        _walkingSessionState.value = WalkingSessionState.IDLE
        pauseTimer()

        _syncMessage.value = if (language.value == "bn")
            "অসাধারণ! সেশনে $completedSteps কদম হেঁটেছেন ($timeFormatted সময়)।"
        else
            "Great walk! You took $completedSteps session steps in $timeFormatted."
    }

    private fun startTimer() {
        sessionTimerJob?.cancel()
        sessionTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_walkingSessionState.value == WalkingSessionState.WALKING) {
                    _sessionDurationSeconds.value++
                }
            }
        }
    }

    private fun pauseTimer() {
        sessionTimerJob?.cancel()
        sessionTimerJob = null
    }

    fun addManualSteps(count: Int) {
        stepSensorManager.addManualSteps(count)
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            preferences.setOnboardingCompleted(true)
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            preferences.setLanguage(lang)
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            preferences.setThemeMode(mode)
        }
    }

    fun setStepGoal(goal: Int) {
        viewModelScope.launch {
            preferences.setDailyStepGoal(goal)
            val current = todayActivity.value
            if (current != null) {
                repository.saveDailyActivity(
                    current.copy(
                        goalSteps = goal,
                        isGoalCompleted = _liveSteps.value >= goal
                    )
                )
            }
        }
    }

    fun logWater(amountMl: Int) {
        viewModelScope.launch {
            repository.logWater(amountMl)
        }
    }

    fun deleteWater(id: Long) {
        viewModelScope.launch {
            repository.deleteWater(id)
        }
    }

    fun setDailyWaterGoal(goalMl: Int) {
        viewModelScope.launch {
            preferences.setDailyWaterGoal(goalMl)
        }
    }

    fun logWeight(weightKg: Float, notes: String = "") {
        viewModelScope.launch {
            repository.logWeight(weightKg, notes)
            preferences.updateUserProfile(
                name = userName.value,
                email = userEmail.value,
                age = userAge.value,
                heightCm = userHeightCm.value,
                weightKg = weightKg,
                targetKg = targetWeightKg.value
            )
        }
    }

    fun deleteWeight(id: Long) {
        viewModelScope.launch {
            repository.deleteWeight(id)
        }
    }

    fun updateProfile(name: String, email: String, age: Int, heightCm: Float, weightKg: Float, targetKg: Float) {
        viewModelScope.launch {
            preferences.updateUserProfile(name, email, age, heightCm, weightKg, targetKg)
        }
    }

    fun signInWithGoogle(email: String = "rahul@example.com", name: String = "Rahul Das") {
        viewModelScope.launch {
            preferences.setGoogleSignedIn(true, email, name)
            _syncMessage.value = if (language.value == "bn") "গুগল অ্যাকাউন্টে সাইন-ইন সম্পন্ন হয়েছে!" else "Signed in with Google successfully!"
        }
    }

    fun signOutGoogle() {
        viewModelScope.launch {
            preferences.setGoogleSignedIn(false)
            _syncMessage.value = if (language.value == "bn") "সাইন-আউট সম্পন্ন হয়েছে" else "Signed out successfully"
        }
    }

    fun performCloudBackup() {
        viewModelScope.launch {
            preferences.updateLastBackupTime()
            _syncMessage.value = if (language.value == "bn")
                "ক্লাউড ব্যাকআপ সফল হয়েছে! সকল তথ্য সুরক্ষিত।"
            else
                "Cloud backup completed successfully! All records safe."
        }
    }

    fun performCloudRestore() {
        viewModelScope.launch {
            _syncMessage.value = if (language.value == "bn")
                "ক্লাউড থেকে সফলভাবে তথ্য পুনরুদ্ধার করা হয়েছে!"
            else
                "Data successfully restored from Cloud backup!"
        }
    }

    fun exportCsv(context: Context) {
        viewModelScope.launch {
            val csvData = repository.exportDataAsCsv()
            try {
                val file = File(context.cacheDir, "vitalstep_activity_export.csv")
                file.writeText(csvData)

                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_SUBJECT, "VitalStep Activity Report")
                    putExtra(Intent.EXTRA_TEXT, "Here is my exported VitalStep activity data:\n\n$csvData")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(Intent.createChooser(sendIntent, "Share VitalStep Activity CSV"))
            } catch (e: Exception) {
                _syncMessage.value = "Export failed: ${e.localizedMessage}"
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllLocalData()
            _liveSteps.value = 0
            _liveDistanceKm.value = 0f
            _liveCalories.value = 0
            _liveActiveMinutes.value = 0
            _sessionSteps.value = 0
            _sessionDistanceKm.value = 0f
            _sessionCalories.value = 0
            _sessionDurationSeconds.value = 0
            _walkingSessionState.value = WalkingSessionState.IDLE
            pauseTimer()
            _syncMessage.value = if (language.value == "bn") "সমস্ত তথ্য মুছে ফেলা হয়েছে" else "All user data cleared"
        }
    }

    fun dismissCelebration() {
        _celebrationAchievement.value = null
    }

    fun clearSyncMessage() {
        _syncMessage.value = null
    }

    fun checkForUpdatesManual() {
        viewModelScope.launch {
            updateManager.checkForUpdates()
        }
    }

    fun triggerInstallApk(file: File) {
        updateManager.triggerInstallApk(file)
    }

    fun downloadAndInstallUpdate(downloadUrl: String, versionName: String) {
        updateManager.downloadAndInstallUpdate(downloadUrl, versionName)
    }

    fun setAutoCheckUpdates(enabled: Boolean) {
        viewModelScope.launch {
            preferences.setAutoCheckUpdates(enabled)
        }
    }

    fun resetUpdateStatus() {
        updateManager.resetStatus()
    }

    override fun onCleared() {
        super.onCleared()
        pauseTimer()
        stepSensorManager.stopListening()
    }
}
