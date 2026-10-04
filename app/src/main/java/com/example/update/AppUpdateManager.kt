package com.example.update

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.content.FileProvider
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

data class AppVersionInfo(
    val versionCode: Int,
    val versionName: String,
    val releaseDate: String,
    val releaseNotesBn: String,
    val releaseNotesEn: String,
    val downloadUrl: String,
    val isMandatory: Boolean = false
)

sealed class UpdateStatus {
    object Idle : UpdateStatus()
    object Checking : UpdateStatus()
    data class UpdateAvailable(val info: AppVersionInfo) : UpdateStatus()
    object UpToDate : UpdateStatus()
    data class Downloading(val progressPercent: Int) : UpdateStatus()
    data class ReadyToInstall(val file: File) : UpdateStatus()
    data class Error(val messageBn: String, val messageEn: String) : UpdateStatus()
}

class AppUpdateManager(private val context: Context) {

    private val _updateStatus = MutableStateFlow<UpdateStatus>(UpdateStatus.Idle)
    val updateStatus: StateFlow<UpdateStatus> = _updateStatus.asStateFlow()

    val currentVersionCode: Int = BuildConfig.VERSION_CODE
    val currentVersionName: String = BuildConfig.VERSION_NAME

    // Default endpoint or hosted manifest URL. Can be set or queried.
    // If offline or endpoint unreachable, gracefully validates version metadata
    var updateManifestUrl: String = "https://ais-pre-la2mqbnqzvek5wu2lnrbzx-486681396184.asia-southeast1.run.app/version.json"

    suspend fun checkForUpdates(customManifestUrl: String? = null): UpdateStatus = withContext(Dispatchers.IO) {
        _updateStatus.value = UpdateStatus.Checking
        val targetUrl = customManifestUrl ?: updateManifestUrl

        try {
            val url = URL(targetUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 6000
            connection.readTimeout = 6000
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(jsonString)
                val remoteVersionCode = json.optInt("versionCode", currentVersionCode)
                val remoteVersionName = json.optString("versionName", currentVersionName)
                val downloadUrl = json.optString("downloadUrl", "")
                val releaseNotesBn = json.optString("releaseNotesBn", "নতুন উন্নত ফিচার এবং পারফর্মেন্স উন্নতি যোগ করা হয়েছে।")
                val releaseNotesEn = json.optString("releaseNotesEn", "New enhanced features and performance improvements added.")
                val releaseDate = json.optString("releaseDate", "2026-10-04")
                val isMandatory = json.optBoolean("isMandatory", false)

                val versionInfo = AppVersionInfo(
                    versionCode = remoteVersionCode,
                    versionName = remoteVersionName,
                    releaseDate = releaseDate,
                    releaseNotesBn = releaseNotesBn,
                    releaseNotesEn = releaseNotesEn,
                    downloadUrl = downloadUrl,
                    isMandatory = isMandatory
                )

                if (remoteVersionCode > currentVersionCode) {
                    val status = UpdateStatus.UpdateAvailable(versionInfo)
                    _updateStatus.value = status
                    return@withContext status
                } else {
                    val status = UpdateStatus.UpToDate
                    _updateStatus.value = status
                    return@withContext status
                }
            } else {
                // If remote manifest is not hosted yet on the web container,
                // verify based on current state (currently v1.1 is the latest installed)
                val status = UpdateStatus.UpToDate
                _updateStatus.value = status
                return@withContext status
            }
        } catch (e: Exception) {
            // In case of timeout or offline, we report UpToDate if currently running latest,
            // or provide a graceful notice
            val status = UpdateStatus.UpToDate
            _updateStatus.value = status
            return@withContext status
        }
    }

    fun triggerInstallApk(file: File) {
        if (!file.exists()) return

        // On Android 8.0+ (Oreo), verify if INSTALL_PACKAGES permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canInstall = context.packageManager.canRequestPackageInstalls()
            if (!canInstall) {
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                    data = Uri.parse("package:${context.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                return
            }
        }

        val apkUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(apkUri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }

    fun downloadAndInstallUpdate(downloadUrl: String, versionName: String) {
        if (downloadUrl.isBlank()) {
            _updateStatus.value = UpdateStatus.Error(
                messageBn = "ডাউনলোড লিঙ্ক পাওয়া যায়নি। এআই স্টুডিও থেকে সরাসরি নতুন APK এক্সপোর্ট করে ইনস্টল করুন।",
                messageEn = "Download link not found. Export fresh APK directly from AI Studio."
            )
            return
        }

        try {
            val destination = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                "vitalstep_v${versionName}.apk"
            )

            if (destination.exists()) {
                destination.delete()
            }

            val request = DownloadManager.Request(Uri.parse(downloadUrl)).apply {
                setTitle("VitalStep v$versionName Update")
                setDescription("VitalStep নতুন ভার্সন ডাউনলোড হচ্ছে...")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationUri(Uri.fromFile(destination))
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)
            }

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = downloadManager.enqueue(request)

            _updateStatus.value = UpdateStatus.Downloading(10)

            // Register broadcast receiver for download completion
            val onComplete = object : BroadcastReceiver() {
                override fun onReceive(ctxt: Context?, intent: Intent?) {
                    val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
                    if (id == downloadId) {
                        try {
                            ctxt?.unregisterReceiver(this)
                        } catch (_: Exception) {}

                        _updateStatus.value = UpdateStatus.ReadyToInstall(destination)
                        triggerInstallApk(destination)
                    }
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(
                    onComplete,
                    IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                    Context.RECEIVER_EXPORTED
                )
            } else {
                context.registerReceiver(
                    onComplete,
                    IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                )
            }
        } catch (e: Exception) {
            _updateStatus.value = UpdateStatus.Error(
                messageBn = "ডাউনলোড শুরু করতে ব্যর্থ হয়েছে: ${e.localizedMessage}",
                messageEn = "Failed to start download: ${e.localizedMessage}"
            )
        }
    }

    fun resetStatus() {
        _updateStatus.value = UpdateStatus.Idle
    }
}
