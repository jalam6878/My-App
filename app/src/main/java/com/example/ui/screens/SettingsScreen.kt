package com.example.ui.screens

import android.content.Context
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.GoldenSun
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.RoseHeart
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.getGlassBackground
import com.example.update.AppVersionInfo
import com.example.update.UpdateStatus
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SettingsScreen(
    themeMode: String,
    language: String,
    isGoogleSignedIn: Boolean,
    lastBackupTime: Long,
    appVersionName: String = "1.1",
    appVersionCode: Int = 2,
    autoCheckUpdates: Boolean = true,
    updateStatus: UpdateStatus = UpdateStatus.Idle,
    onCheckForUpdates: () -> Unit = {},
    onToggleAutoCheckUpdates: (Boolean) -> Unit = {},
    onInstallUpdate: (File) -> Unit = {},
    onDownloadUpdate: (String, String) -> Unit = { _, _ -> },
    onResetUpdateStatus: () -> Unit = {},
    onSelectTheme: (String) -> Unit,
    onToggleLanguage: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onBackupData: () -> Unit,
    onExportData: (Context) -> Unit,
    onClearData: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val isBengali = language == "bn"
    val isDark = LocalIsDarkTheme.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showThemeDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showUpdateGuideDialog by remember { mutableStateOf(false) }

    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    val currentThemeLabel = when (themeMode) {
        "light" -> if (isBengali) "হোয়াইট গ্লাস লাইট" else "White Liquid Glass"
        "system" -> if (isBengali) "সিস্টেম ডিফল্ট" else "System Default"
        else -> if (isBengali) "ডার্ক গ্লাস" else "Midnight Dark Glass"
    }

    val backupDateStr = if (lastBackupTime > 0) {
        SimpleDateFormat("d MMM yyyy, h:mm a", Locale.getDefault()).format(Date(lastBackupTime))
    } else {
        if (isBengali) "কোনো ব্যাকআপ নেই" else "No backup yet"
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
                        .background(if (isDark) Color(0x331E293B) else Color(0x2294A3B8))
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
                    text = if (isBengali) "সেটিংস" else "Settings",
                    color = primaryText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Theme Selection Row
                SettingsRow(
                    icon = when (themeMode) {
                        "light" -> Icons.Default.LightMode
                        "system" -> Icons.Default.BrightnessAuto
                        else -> Icons.Default.DarkMode
                    },
                    title = if (isBengali) "থিম মোড" else "Theme Mode",
                    trailingText = currentThemeLabel,
                    accentColor = NeonPurple,
                    isDark = isDark,
                    onClick = { showThemeDialog = true }
                )

                // Language
                SettingsRow(
                    icon = Icons.Default.Language,
                    title = if (isBengali) "ভাষা" else "Language",
                    trailingText = if (isBengali) "বাংলা" else "English",
                    accentColor = GoldenSun,
                    isDark = isDark,
                    onClick = onToggleLanguage
                )

                // Notifications
                SettingsRow(
                    icon = Icons.Default.Notifications,
                    title = if (isBengali) "বিজ্ঞপ্তি সেটিংস" else "Notifications",
                    trailingText = "",
                    accentColor = ElectricBlue,
                    isDark = isDark,
                    onClick = onNavigateToNotifications
                )

                // Data Backup
                SettingsRow(
                    icon = Icons.Default.CloudUpload,
                    title = if (isBengali) "ক্লাউড ব্যাকআপ" else "Data Backup",
                    trailingText = backupDateStr,
                    accentColor = EmeraldGlow,
                    isDark = isDark,
                    onClick = onBackupData
                )

                // Export Data
                SettingsRow(
                    icon = Icons.Default.FileDownload,
                    title = if (isBengali) "তথ্য এক্সপোর্ট (CSV)" else "Export Data",
                    trailingText = "",
                    accentColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                    isDark = isDark,
                    onClick = { onExportData(context) }
                )

                // Privacy
                SettingsRow(
                    icon = Icons.Default.Security,
                    title = if (isBengali) "গোপনীয়তা ও স্বাস্থ্য নিরাপত্তা" else "Privacy & Disclaimer",
                    trailingText = "",
                    accentColor = CyanAccent,
                    isDark = isDark,
                    onClick = { showPrivacyDialog = true }
                )

                // App Version & In-App Update Checker
                SettingsRow(
                    icon = Icons.Default.SystemUpdate,
                    title = if (isBengali) "অ্যাপ আপডেট ও সংস্করণ" else "App Updates & Version",
                    trailingText = "v$appVersionName (Build $appVersionCode)",
                    accentColor = if (isDark) NeonCyan else Color(0xFF0284C7),
                    isDark = isDark,
                    onClick = { onCheckForUpdates() }
                )

                // How Auto-Updates Work Guide
                SettingsRow(
                    icon = Icons.Default.HelpOutline,
                    title = if (isBengali) "অটো-আপডেট কীভাবে কাজ করে?" else "How Auto-Updates Work",
                    trailingText = "",
                    accentColor = ElectricBlue,
                    isDark = isDark,
                    onClick = { showUpdateGuideDialog = true }
                )

                // Interactive Update Status Feedback Card
                when (val status = updateStatus) {
                    is UpdateStatus.Checking -> {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = NeonCyan,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (isBengali) "নতুন আপডেট অনুসন্ধান করা হচ্ছে..." else "Checking for updates...",
                                    color = primaryText,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                    is UpdateStatus.UpdateAvailable -> {
                        val info = status.info
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            borderColor = EmeraldGlow
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.SystemUpdate,
                                            contentDescription = null,
                                            tint = EmeraldGlow,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isBengali) "নতুন আপডেট: v${info.versionName}" else "New Update: v${info.versionName}",
                                            color = EmeraldGlow,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    TextButton(onClick = { onResetUpdateStatus() }) {
                                        Text(if (isBengali) "মুছুন" else "Dismiss", color = secondaryText, fontSize = 12.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = if (isBengali) info.releaseNotesBn else info.releaseNotesEn,
                                    color = primaryText,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                GlassButton(
                                    text = if (isBengali) "আপডেট ডাউনলোড ও ইনস্টল করুন" else "Download & Install Update",
                                    onClick = { onDownloadUpdate(info.downloadUrl, info.versionName) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                    is UpdateStatus.Downloading -> {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = if (isBengali) "নতুন ভার্সন ডাউনলোড হচ্ছে..." else "Downloading update...",
                                    color = primaryText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = NeonCyan
                                )
                            }
                        }
                    }
                    is UpdateStatus.ReadyToInstall -> {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            borderColor = EmeraldGlow
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = if (isBengali) "✓ ডাউনলোড সম্পন্ন! এখনই ইনস্টল করুন" else "✓ Download complete! Ready to install",
                                    color = EmeraldGlow,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                GlassButton(
                                    text = if (isBengali) "ইনস্টল করুন (Install Now)" else "Install Now",
                                    onClick = { onInstallUpdate(status.file) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                    is UpdateStatus.UpToDate -> {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = EmeraldGlow,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isBengali) "আপনার অ্যাপটি সর্বশেষ সংস্করণে আপডেট রয়েছে" else "Your app is up to date (v$appVersionName)",
                                        color = EmeraldGlow,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                TextButton(onClick = { onResetUpdateStatus() }) {
                                    Text("OK", color = NeonCyan, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                    is UpdateStatus.Error -> {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (isBengali) status.messageBn else status.messageEn,
                                    color = RoseHeart,
                                    fontSize = 12.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(onClick = { onResetUpdateStatus() }) {
                                    Text("OK", color = NeonCyan, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                    UpdateStatus.Idle -> {}
                }

                // About App
                SettingsRow(
                    icon = Icons.Default.Info,
                    title = if (isBengali) "অ্যাপ পরিচিতি" else "About App",
                    trailingText = "v$appVersionName",
                    accentColor = GoldenSun,
                    isDark = isDark,
                    onClick = { showAboutDialog = true }
                )

                // Delete My Data
                SettingsRow(
                    icon = Icons.Default.DeleteForever,
                    title = if (isBengali) "সকল তথ্য মুছে ফেলুন" else "Delete My Data",
                    trailingText = "",
                    accentColor = RoseHeart,
                    isDark = isDark,
                    onClick = { showDeleteConfirmDialog = true }
                )
            }
        }

        // Theme Selection Dialog (Dark, Light, System Default)
        if (showThemeDialog) {
            AlertDialog(
                onDismissRequest = { showThemeDialog = false },
                containerColor = if (isDark) Color(0xFF0F172A) else Color(0xFFF8FAFC),
                title = {
                    Text(
                        text = if (isBengali) "থিম নির্বাচন করুন" else "Select Theme Mode",
                        color = primaryText,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        ThemeOptionTile(
                            icon = Icons.Default.DarkMode,
                            title = if (isBengali) "ডার্ক মোড (Midnight Glass)" else "Dark Mode (Midnight Glass)",
                            isSelected = themeMode == "dark",
                            isDark = isDark,
                            onClick = {
                                onSelectTheme("dark")
                                showThemeDialog = false
                            }
                        )

                        ThemeOptionTile(
                            icon = Icons.Default.LightMode,
                            title = if (isBengali) "লাইট মোড (White Liquid Glass)" else "Light Mode (White Liquid Glass)",
                            isSelected = themeMode == "light",
                            isDark = isDark,
                            onClick = {
                                onSelectTheme("light")
                                showThemeDialog = false
                            }
                        )

                        ThemeOptionTile(
                            icon = Icons.Default.BrightnessAuto,
                            title = if (isBengali) "সিস্টেম ডিফল্ট" else "System Default",
                            isSelected = themeMode == "system",
                            isDark = isDark,
                            onClick = {
                                onSelectTheme("system")
                                showThemeDialog = false
                            }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showThemeDialog = false }) {
                        Text(if (isBengali) "বাতিল" else "Cancel", color = NeonCyan)
                    }
                }
            )
        }

        // Privacy & Medical Disclaimer Dialog
        if (showPrivacyDialog) {
            AlertDialog(
                onDismissRequest = { showPrivacyDialog = false },
                containerColor = if (isDark) Color(0xFF0F172A) else Color(0xFFF8FAFC),
                title = {
                    Text(
                        text = if (isBengali) "গোপনীয়তা ও স্বাস্থ্য নিরাপত্তা" else "Privacy & Medical Disclaimer",
                        color = primaryText,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(
                            text = if (isBengali)
                                "১. গোপনীয়তা নীতি:\nVitalStep আপনার কোনো ব্যক্তিগত স্বাস্থ্য তথ্য বিক্রি বা বিজ্ঞাপনে ব্যবহার করে না। সকল তথ্য আপনার ডিভাইসে নিরাপদে সংরক্ষিত থাকে।\n\n২. স্বাস্থ্য সতর্কতা:\nএই অ্যাপটি শুধুমাত্র সাধারণ শারীরিক সুস্থতা ও সচেতনতামূলক তথ্য প্রদান করে। এটি কোনো রোগ নির্ণয় বা চিকিৎসার বিকল্প নয়। জরুরি শারীরিক উপসর্গের ক্ষেত্রে অনতিবিলম্বে বিশেষজ্ঞ চিকিৎসকের পরামর্শ নিন।"
                            else
                                "1. Privacy Policy:\nVitalStep values your confidentiality. We never sell your personal health records or use them for third-party advertisements. Data is securely retained on your device.\n\n2. Health Safety Disclaimer:\nVitalStep provides educational guidance only and is not a substitute for professional medical diagnosis or treatment. Always consult certified physicians before embarking on intense regimens or if you experience warning symptoms.",
                            color = secondaryText,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showPrivacyDialog = false }) {
                        Text("OK", color = NeonCyan, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // About Dialog
        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                containerColor = if (isDark) Color(0xFF0F172A) else Color(0xFFF8FAFC),
                title = {
                    Text(
                        text = "VITALSTEP",
                        color = primaryText,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Every Step. Every Day. A Healthier You.",
                            color = CyanAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Version: v$appVersionName (Build $appVersionCode)\n100% Free • No Subscription • Zero Ads\nPowered by Android Sensor Framework & Room Database.",
                            color = secondaryText,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) {
                        Text("Close", color = NeonCyan)
                    }
                }
            )
        }

        // Auto-Update Guide Dialog
        if (showUpdateGuideDialog) {
            AlertDialog(
                onDismissRequest = { showUpdateGuideDialog = false },
                containerColor = if (isDark) Color(0xFF0F172A) else Color(0xFFF8FAFC),
                icon = {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0x3300F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SystemUpdate,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                title = {
                    Text(
                        text = if (isBengali) "স্বয়ংক্রিয় আপডেট কীভাবে কাজ করে?" else "How Updates Work on Mobile",
                        color = primaryText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = if (isBengali)
                                "১. নতুন ফিচার যোগ করার পর মোবাইলে নেওয়ার নিয়ম:\nআপনি যখন Google AI Studio-তে নতুন কোনো ফিচার যুক্ত করবেন, উপরের ডানদিকের সেটিংস থেকে 'Export APK' নির্বাচন করে নতুন APK-টি নামিয়ে আপনার মোবাইলে ওপেন করুন। 'Update' চাপলে আপনার কোনো আগের ডেটা (হাঁটার স্টেপ, ওজন, পানি ইত্যাদি) নষ্ট না হয়ে নিরাপদে নতুন ভার্সন আপডেট হয়ে যাবে।\n\n২. ইন-অ্যাপ সরাসরি অটো-আপডেট:\nঅ্যাপটিতে ইন-অ্যাপ আপডেট চেকার সক্রিয় রয়েছে। 'অ্যাপ আপডেট ও সংস্করণ' বাটনে চাপলে এটি সার্ভারে নতুন ভার্সন আছে কিনা পরীক্ষা করবে এবং পাওয়া গেলে সরাসরি ডাউনলোড করে ইনস্টল করার সুযোগ দেবে।\n\n৩. গুগল প্লে স্টোর অটো-আপডেট:\nঅ্যাপটি ভবিষ্যতে Google Play Store-এ আপলোড করা হলে, প্লে স্টোর স্বয়ংক্রিয়ভাবে ব্যাকগ্রাউন্ডে আপনার মোবাইলে নতুন সব আপডেট ইনস্টল করে দেবে।"
                            else
                                "1. How to get updates after modifying code:\nWhen you add new features in Google AI Studio, choose 'Export APK' from the project settings menu. Open the downloaded APK on your mobile phone and tap 'Update'. All your existing records (steps, hydration, weight history) are completely preserved!\n\n2. In-App Direct Updater:\nThe app features a built-in Over-The-Air update system. Tapping 'App Updates & Version' checks for new updates and lets you download & install with a single click.\n\n3. Google Play Store Updates:\nOnce published to Google Play Store, Android automatically downloads and installs new releases in the background without manual steps.",
                            color = secondaryText,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showUpdateGuideDialog = false }) {
                        Text(if (isBengali) "বুঝেছি (Got It)" else "Got It", color = NeonCyan, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Delete Data Dialog
        if (showDeleteConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmDialog = false },
                containerColor = if (isDark) Color(0xFF0F172A) else Color(0xFFF8FAFC),
                title = {
                    Text(
                        text = if (isBengali) "আপনি কি নিশ্চিত?" else "Delete All Data?",
                        color = RoseHeart,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = if (isBengali)
                            "এই প্রক্রিয়ায় আপনার সকল সংরক্ষিত পদক্ষেপের ইতিহাস, ওজন ও পানি পানের রেকর্ড মুছে যাবে। এই পরিবর্তনটি পূর্বাবস্থায় ফেরানো যাবে না।"
                        else
                            "This will permanently erase all your saved walking history, weights, and hydration records from this device. This cannot be undone.",
                        color = secondaryText,
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onClearData()
                            showDeleteConfirmDialog = false
                        }
                    ) {
                        Text(if (isBengali) "মুছে ফেলুন" else "Delete", color = RoseHeart, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmDialog = false }) {
                        Text(if (isBengali) "বাতিল" else "Cancel", color = secondaryText)
                    }
                }
            )
        }
    }
}

@Composable
fun ThemeOptionTile(
    icon: ImageVector,
    title: String,
    isSelected: Boolean,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val primaryText = if (isDark) TextWhite else TextDark
    val bg = if (isSelected) {
        if (isDark) Color(0x3300F2FE) else Color(0x260284C7)
    } else {
        if (isDark) Color(0x221E293B) else Color(0x1F94A3B8)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .border(
                1.dp,
                if (isSelected) NeonCyan else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) NeonCyan else primaryText,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    color = primaryText,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = NeonCyan,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    trailingText: String,
    accentColor: Color,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val primaryText = if (isDark) TextWhite else TextDark
    val secondaryText = if (isDark) TextMuted else TextDarkMuted

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    color = primaryText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (trailingText.isNotEmpty()) {
                    Text(
                        text = trailingText,
                        color = secondaryText,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = secondaryText,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
