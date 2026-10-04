package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AchievementEntity
import com.example.ui.components.AchievementCelebrationDialog
import com.example.ui.components.BottomNavTab
import com.example.ui.components.FloatingBottomBar
import com.example.ui.components.StepGoalDialog
import com.example.ui.screens.AchievementsScreen
import com.example.ui.screens.ActivityScreen
import com.example.ui.screens.BmiCalculatorScreen
import com.example.ui.screens.DietPlanScreen
import com.example.ui.screens.EditProfileScreen
import com.example.ui.screens.ExerciseGuideScreen
import com.example.ui.screens.HealthScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MotivationScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WaterTrackerScreen
import com.example.ui.screens.WeightTrackerScreen
import com.example.ui.screens.WellnessReportScreen
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.VitalStepTheme
import com.example.ui.theme.getGlassBackground
import com.example.ui.viewmodel.MainViewModel
import com.example.update.UpdateStatus
import kotlinx.coroutines.launch

enum class AppScreen {
    SPLASH,
    ONBOARDING,
    MAIN,
    DIET_PLAN,
    BMI_CALCULATOR,
    HISTORY,
    WATER_TRACKER,
    WEIGHT_TRACKER,
    EXERCISE_GUIDE,
    WELLNESS_REPORT,
    MOTIVATION,
    NOTIFICATIONS,
    SETTINGS,
    EDIT_PROFILE
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: MainViewModel = viewModel()
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()
            val isDarkTheme = when (themeMode) {
                "light" -> false
                "dark" -> true
                else -> systemDark
            }

            VitalStepTheme(darkTheme = isDarkTheme) {
                VitalStepApp(viewModel = viewModel, isDarkTheme = isDarkTheme)
            }
        }
    }
}

@Composable
fun VitalStepApp(viewModel: MainViewModel, isDarkTheme: Boolean) {
    val isOnboardingDone by viewModel.isOnboardingCompleted.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val stepGoal by viewModel.stepGoal.collectAsStateWithLifecycle()
    val waterGoal by viewModel.waterGoal.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val userEmail by viewModel.userEmail.collectAsStateWithLifecycle()
    val userAge by viewModel.userAge.collectAsStateWithLifecycle()
    val userHeightCm by viewModel.userHeightCm.collectAsStateWithLifecycle()
    val userWeightKg by viewModel.userWeightKg.collectAsStateWithLifecycle()
    val targetWeightKg by viewModel.targetWeightKg.collectAsStateWithLifecycle()
    val isGoogleSignedIn by viewModel.isGoogleSignedIn.collectAsStateWithLifecycle()
    val lastBackupTime by viewModel.lastBackupTime.collectAsStateWithLifecycle()

    val liveSteps by viewModel.liveSteps.collectAsStateWithLifecycle()
    val liveDistanceKm by viewModel.liveDistanceKm.collectAsStateWithLifecycle()
    val liveCalories by viewModel.liveCalories.collectAsStateWithLifecycle()
    val liveActiveMinutes by viewModel.liveActiveMinutes.collectAsStateWithLifecycle()

    val walkingSessionState by viewModel.walkingSessionState.collectAsStateWithLifecycle()
    val sessionSteps by viewModel.sessionSteps.collectAsStateWithLifecycle()
    val sessionDistanceKm by viewModel.sessionDistanceKm.collectAsStateWithLifecycle()
    val sessionCalories by viewModel.sessionCalories.collectAsStateWithLifecycle()
    val sessionDurationSeconds by viewModel.sessionDurationSeconds.collectAsStateWithLifecycle()

    val allActivities by viewModel.allActivities.collectAsStateWithLifecycle()
    val allAchievements by viewModel.allAchievements.collectAsStateWithLifecycle()
    val latestAchievement by viewModel.latestUnlockedAchievement.collectAsStateWithLifecycle()
    val waterToday by viewModel.waterToday.collectAsStateWithLifecycle()
    val allWeights by viewModel.allWeights.collectAsStateWithLifecycle()
    val celebrationAchievement by viewModel.celebrationAchievement.collectAsStateWithLifecycle()
    val syncMessage by viewModel.syncMessage.collectAsStateWithLifecycle()

    val notifMorning by viewModel.notifMorningTip.collectAsStateWithLifecycle()
    val notifWater by viewModel.notifWaterReminder.collectAsStateWithLifecycle()
    val notifWalk by viewModel.notifWalkingReminder.collectAsStateWithLifecycle()
    val notifGoal by viewModel.notifGoalAchievement.collectAsStateWithLifecycle()
    val notifEvening by viewModel.notifEveningWellness.collectAsStateWithLifecycle()
    val notifSleep by viewModel.notifSleepReminder.collectAsStateWithLifecycle()

    val isSensorAvailable by viewModel.stepSensorManager.isSensorAvailable.collectAsStateWithLifecycle()
    val updateStatus by viewModel.updateStatus.collectAsStateWithLifecycle()
    val autoCheckUpdates by viewModel.autoCheckUpdates.collectAsStateWithLifecycle()

    var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }
    var currentTab by remember { mutableStateOf(BottomNavTab.HOME) }
    var showStepGoalDialog by remember { mutableStateOf(false) }
    var viewingCelebration by remember { mutableStateOf<AchievementEntity?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val isBengali = language == "bn"

    // Runtime Permission launchers
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        // Handle result
    }

    LaunchedEffect(Unit) {
        val permsToRequest = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permsToRequest.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (permsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permsToRequest.toTypedArray())
        }
    }

    LaunchedEffect(syncMessage) {
        syncMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSyncMessage()
        }
    }

    // Handle Celebration achievement popup
    LaunchedEffect(celebrationAchievement) {
        celebrationAchievement?.let {
            viewingCelebration = it
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (currentScreen == AppScreen.MAIN) {
                FloatingBottomBar(
                    currentTab = currentTab,
                    isBengali = isBengali,
                    onTabSelected = { currentTab = it }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(getGlassBackground(isDarkTheme))
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.SPLASH -> {
                    SplashScreen(
                        onSplashFinished = {
                            currentScreen = if (isOnboardingDone) AppScreen.MAIN else AppScreen.ONBOARDING
                        }
                    )
                }

                AppScreen.ONBOARDING -> {
                    OnboardingScreen(
                        currentLanguage = language,
                        onLanguageSelected = { viewModel.setLanguage(it) },
                        onGetStarted = {
                            viewModel.completeOnboarding()
                            currentScreen = AppScreen.MAIN
                        }
                    )
                }

                AppScreen.MAIN -> {
                    when (currentTab) {
                        BottomNavTab.HOME -> {
                            HomeScreen(
                                currentSteps = liveSteps,
                                distanceKm = liveDistanceKm,
                                calories = liveCalories,
                                activeMinutes = liveActiveMinutes,
                                stepGoal = stepGoal,
                                language = language,
                                latestAchievement = latestAchievement,
                                isSensorAvailable = isSensorAvailable,
                                walkingSessionState = walkingSessionState,
                                sessionSteps = sessionSteps,
                                sessionDistanceKm = sessionDistanceKm,
                                sessionCalories = sessionCalories,
                                sessionDurationSeconds = sessionDurationSeconds,
                                onStartWalkingSession = { viewModel.startWalkingSession() },
                                onPauseWalkingSession = { viewModel.pauseWalkingSession() },
                                onResumeWalkingSession = { viewModel.resumeWalkingSession() },
                                onFinishWalkingSession = { viewModel.finishWalkingSession() },
                                onManualStepIncrement = { viewModel.addManualSteps(it) },
                                onNavigateToActivity = { currentTab = BottomNavTab.ACTIVITY },
                                onNavigateToAchievements = { currentTab = BottomNavTab.ACHIEVEMENTS },
                                onNavigateToWater = { currentScreen = AppScreen.WATER_TRACKER },
                                onNavigateToBmi = { currentScreen = AppScreen.BMI_CALCULATOR },
                                onNavigateToMotivation = { currentScreen = AppScreen.MOTIVATION }
                            )
                        }

                        BottomNavTab.ACTIVITY -> {
                            ActivityScreen(
                                currentSteps = liveSteps,
                                distanceKm = liveDistanceKm,
                                calories = liveCalories,
                                activeMinutes = liveActiveMinutes,
                                stepGoal = stepGoal,
                                recentActivities = allActivities,
                                language = language,
                                onNavigateBack = { currentTab = BottomNavTab.HOME }
                            )
                        }

                        BottomNavTab.ACHIEVEMENTS -> {
                            AchievementsScreen(
                                achievements = allAchievements,
                                language = language,
                                onAchievementClicked = { viewingCelebration = it }
                            )
                        }

                        BottomNavTab.HEALTH -> {
                            HealthScreen(
                                language = language,
                                onNavigateToDietPlan = { currentScreen = AppScreen.DIET_PLAN },
                                onNavigateToExerciseGuide = { currentScreen = AppScreen.EXERCISE_GUIDE },
                                onNavigateToBmi = { currentScreen = AppScreen.BMI_CALCULATOR },
                                onNavigateToWater = { currentScreen = AppScreen.WATER_TRACKER }
                            )
                        }

                        BottomNavTab.PROFILE -> {
                            ProfileScreen(
                                userName = userName,
                                userEmail = userEmail,
                                stepGoal = stepGoal,
                                language = language,
                                isGoogleSignedIn = isGoogleSignedIn,
                                onNavigateToEditProfile = { currentScreen = AppScreen.EDIT_PROFILE },
                                onNavigateToSettings = { currentScreen = AppScreen.SETTINGS },
                                onNavigateToReports = { currentScreen = AppScreen.WELLNESS_REPORT },
                                onNavigateToNotifications = { currentScreen = AppScreen.NOTIFICATIONS },
                                onToggleLanguage = {
                                    viewModel.setLanguage(if (language == "en") "bn" else "en")
                                },
                                onStepGoalClicked = { showStepGoalDialog = true }
                            )
                        }
                    }
                }

                AppScreen.DIET_PLAN -> {
                    BackHandler { currentScreen = AppScreen.MAIN }
                    DietPlanScreen(
                        language = language,
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }

                AppScreen.BMI_CALCULATOR -> {
                    BackHandler { currentScreen = AppScreen.MAIN }
                    BmiCalculatorScreen(
                        currentHeightCm = userHeightCm,
                        currentWeightKg = userWeightKg,
                        language = language,
                        onSaveHeightWeight = { h, w ->
                            viewModel.updateProfile(userName, userEmail, userAge, h, w, targetWeightKg)
                        },
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }

                AppScreen.HISTORY -> {
                    BackHandler { currentScreen = AppScreen.MAIN }
                    HistoryScreen(
                        activities = allActivities,
                        language = language,
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }

                AppScreen.WATER_TRACKER -> {
                    BackHandler { currentScreen = AppScreen.MAIN }
                    WaterTrackerScreen(
                        currentWaterMl = waterToday,
                        targetWaterMl = waterGoal,
                        language = language,
                        onAddWater = { viewModel.logWater(it) },
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }

                AppScreen.WEIGHT_TRACKER -> {
                    BackHandler { currentScreen = AppScreen.MAIN }
                    WeightTrackerScreen(
                        currentWeightKg = userWeightKg,
                        targetWeightKg = targetWeightKg,
                        weightHistory = allWeights,
                        language = language,
                        onRecordWeight = { w, n -> viewModel.logWeight(w, n) },
                        onDeleteWeight = { viewModel.deleteWeight(it) },
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }

                AppScreen.EXERCISE_GUIDE -> {
                    BackHandler { currentScreen = AppScreen.MAIN }
                    ExerciseGuideScreen(
                        language = language,
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }

                AppScreen.WELLNESS_REPORT -> {
                    BackHandler { currentScreen = AppScreen.MAIN }
                    WellnessReportScreen(
                        activities = allActivities,
                        language = language,
                        onExportCsv = { ctx -> viewModel.exportCsv(ctx) },
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }

                AppScreen.MOTIVATION -> {
                    BackHandler { currentScreen = AppScreen.MAIN }
                    MotivationScreen(
                        language = language,
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }

                AppScreen.NOTIFICATIONS -> {
                    BackHandler { currentScreen = AppScreen.MAIN }
                    NotificationsScreen(
                        language = language,
                        notifMorning = notifMorning,
                        notifWater = notifWater,
                        notifWalk = notifWalk,
                        notifGoal = notifGoal,
                        notifEvening = notifEvening,
                        notifSleep = notifSleep,
                        onToggleMorning = {
                            scope.launch { viewModel.preferences.updateNotification(com.example.data.local.UserPreferences.NOTIF_MORNING_TIP, it) }
                        },
                        onToggleWater = {
                            scope.launch { viewModel.preferences.updateNotification(com.example.data.local.UserPreferences.NOTIF_WATER_REMINDER, it) }
                        },
                        onToggleWalk = {
                            scope.launch { viewModel.preferences.updateNotification(com.example.data.local.UserPreferences.NOTIF_WALKING_REMINDER, it) }
                        },
                        onToggleGoal = {
                            scope.launch { viewModel.preferences.updateNotification(com.example.data.local.UserPreferences.NOTIF_GOAL_ACHIEVEMENT, it) }
                        },
                        onToggleEvening = {
                            scope.launch { viewModel.preferences.updateNotification(com.example.data.local.UserPreferences.NOTIF_EVENING_WELLNESS, it) }
                        },
                        onToggleSleep = {
                            scope.launch { viewModel.preferences.updateNotification(com.example.data.local.UserPreferences.NOTIF_SLEEP_REMINDER, it) }
                        },
                        onTestNotification = {
                            viewModel.notificationHelper.showGoalReachedNotification(liveSteps, stepGoal, isBengali)
                        },
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }

                AppScreen.SETTINGS -> {
                    BackHandler { currentScreen = AppScreen.MAIN }
                    SettingsScreen(
                        themeMode = themeMode,
                        language = language,
                        isGoogleSignedIn = isGoogleSignedIn,
                        lastBackupTime = lastBackupTime,
                        appVersionName = viewModel.currentAppVersionName,
                        appVersionCode = viewModel.currentAppVersionCode,
                        autoCheckUpdates = autoCheckUpdates,
                        updateStatus = updateStatus,
                        onCheckForUpdates = { viewModel.checkForUpdatesManual() },
                        onToggleAutoCheckUpdates = { viewModel.setAutoCheckUpdates(it) },
                        onInstallUpdate = { viewModel.triggerInstallApk(it) },
                        onDownloadUpdate = { url, vName -> viewModel.downloadAndInstallUpdate(url, vName) },
                        onResetUpdateStatus = { viewModel.resetUpdateStatus() },
                        onSelectTheme = { viewModel.setThemeMode(it) },
                        onToggleLanguage = {
                            viewModel.setLanguage(if (language == "en") "bn" else "en")
                        },
                        onNavigateToNotifications = { currentScreen = AppScreen.NOTIFICATIONS },
                        onBackupData = { viewModel.performCloudBackup() },
                        onExportData = { ctx -> viewModel.exportCsv(ctx) },
                        onClearData = { viewModel.clearAllData() },
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }

                AppScreen.EDIT_PROFILE -> {
                    BackHandler { currentScreen = AppScreen.MAIN }
                    EditProfileScreen(
                        currentName = userName,
                        currentEmail = userEmail,
                        currentAge = userAge,
                        currentHeightCm = userHeightCm,
                        currentWeightKg = userWeightKg,
                        currentTargetKg = targetWeightKg,
                        language = language,
                        onSaveProfile = { n, e, a, h, w, t ->
                            viewModel.updateProfile(n, e, a, h, w, t)
                        },
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }
            }

            // Step Goal Dialog
            if (showStepGoalDialog) {
                StepGoalDialog(
                    currentGoal = stepGoal,
                    isBengali = isBengali,
                    onDismiss = { showStepGoalDialog = false },
                    onGoalSelected = { viewModel.setStepGoal(it) }
                )
            }

            // Achievement Celebration Dialog
            viewingCelebration?.let { ach ->
                AchievementCelebrationDialog(
                    achievement = ach,
                    isBengali = isBengali,
                    onDismiss = {
                        viewingCelebration = null
                        viewModel.dismissCelebration()
                    }
                )
            }

            // Global Update Available Alert Dialog
            if (updateStatus is UpdateStatus.UpdateAvailable) {
                val info = (updateStatus as UpdateStatus.UpdateAvailable).info
                AlertDialog(
                    onDismissRequest = { viewModel.resetUpdateStatus() },
                    containerColor = if (isDarkTheme) Color(0xFF0F172A) else Color(0xFFF8FAFC),
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color(0x3310B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SystemUpdate,
                                contentDescription = null,
                                tint = EmeraldGlow,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    title = {
                        Text(
                            text = if (isBengali) "নতুন আপডেট উপলব্ধ! (v${info.versionName})" else "New Update Available! (v${info.versionName})",
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) TextWhite else TextDark
                        )
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = if (isBengali) "VitalStep-এ নতুন ফিচার ও উন্নতি যুক্ত করা হয়েছে:" else "New features and enhancements are available:",
                                fontSize = 13.sp,
                                color = if (isDarkTheme) TextMuted else TextDarkMuted
                            )
                            Text(
                                text = if (isBengali) info.releaseNotesBn else info.releaseNotesEn,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isDarkTheme) TextWhite else TextDark
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.downloadAndInstallUpdate(info.downloadUrl, info.versionName)
                            }
                        ) {
                            Text(
                                text = if (isBengali) "এখনই আপডেট করুন" else "Update Now",
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { viewModel.resetUpdateStatus() }
                        ) {
                            Text(
                                text = if (isBengali) "পরে" else "Later",
                                color = if (isDarkTheme) TextMuted else TextDarkMuted
                            )
                        }
                    }
                )
            }
        }
    }
}
