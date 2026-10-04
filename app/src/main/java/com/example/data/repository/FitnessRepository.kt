package com.example.data.repository

import com.example.data.local.AchievementDao
import com.example.data.local.AchievementEntity
import com.example.data.local.DailyActivityDao
import com.example.data.local.DailyActivityEntity
import com.example.data.local.WaterDao
import com.example.data.local.WaterIntakeEntity
import com.example.data.local.WeightDao
import com.example.data.local.WeightEntity
import com.example.sensor.StepCounterManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import org.json.JSONObject

class FitnessRepository(
    private val activityDao: DailyActivityDao,
    private val waterDao: WaterDao,
    private val weightDao: WeightDao,
    private val achievementDao: AchievementDao
) {

    val allActivities: Flow<List<DailyActivityEntity>> = activityDao.getAllActivities()
    val totalLifetimeSteps: Flow<Int?> = activityDao.getTotalLifetimeSteps()
    val totalLifetimeDistance: Flow<Float?> = activityDao.getTotalLifetimeDistance()
    val bestStepsDay: Flow<Int?> = activityDao.getBestStepsDay()
    val allAchievements: Flow<List<AchievementEntity>> = achievementDao.getAllAchievements()
    val latestUnlockedAchievement: Flow<AchievementEntity?> = achievementDao.getLatestUnlockedAchievement()
    val unlockedCount: Flow<Int> = achievementDao.getUnlockedCount()
    val allWeights: Flow<List<WeightEntity>> = weightDao.getAllWeightEntries()
    val latestWeight: Flow<WeightEntity?> = weightDao.getLatestWeight()

    fun getActivityForToday(): Flow<DailyActivityEntity?> {
        val today = StepCounterManager.getTodayDateString()
        return activityDao.getActivityForDate(today)
    }

    suspend fun getTodayActivityOnce(): DailyActivityEntity? {
        val today = StepCounterManager.getTodayDateString()
        return activityDao.getActivityForDateOnce(today)
    }

    suspend fun saveDailyActivity(activity: DailyActivityEntity) {
        activityDao.insertOrUpdate(activity)
    }

    fun getWaterToday(): Flow<Int> {
        val today = StepCounterManager.getTodayDateString()
        return waterDao.getTotalWaterForDate(today)
    }

    fun getWaterEntriesToday(): Flow<List<WaterIntakeEntity>> {
        val today = StepCounterManager.getTodayDateString()
        return waterDao.getWaterIntakeForDate(today)
    }

    suspend fun logWater(amountMl: Int) {
        val today = StepCounterManager.getTodayDateString()
        waterDao.insertWaterIntake(
            WaterIntakeEntity(
                date = today,
                amountMl = amountMl
            )
        )
    }

    suspend fun deleteWater(id: Long) {
        waterDao.deleteWaterIntake(id)
    }

    suspend fun logWeight(weightKg: Float, notes: String = "") {
        val today = StepCounterManager.getTodayDateString()
        weightDao.insertWeight(
            WeightEntity(
                date = today,
                weightKg = weightKg,
                notes = notes
            )
        )
    }

    suspend fun deleteWeight(id: Long) {
        weightDao.deleteWeight(id)
    }

    suspend fun seedAchievementsIfEmpty() {
        val all = achievementDao.getAllAchievements().first()
        if (all.isEmpty()) {
            achievementDao.insertAchievements(HealthContentRepository.defaultAchievements)
        }
    }

    suspend fun checkAndUnlockAchievements(currentSteps: Int, consecutiveDays: Int = 1): List<AchievementEntity> {
        val freshlyUnlocked = mutableListOf<AchievementEntity>()
        val achievements = achievementDao.getAllAchievements().first()

        for (ach in achievements) {
            if (!ach.isUnlocked) {
                var shouldUnlock = false
                if (ach.category == "steps" && currentSteps >= ach.targetValue) {
                    shouldUnlock = true
                } else if (ach.category == "streaks" && consecutiveDays >= ach.targetValue) {
                    shouldUnlock = true
                }

                if (shouldUnlock) {
                    achievementDao.unlockAchievement(ach.id)
                    freshlyUnlocked.add(ach.copy(isUnlocked = true, unlockedAt = System.currentTimeMillis()))
                }
            }
        }
        return freshlyUnlocked
    }

    suspend fun exportDataAsJson(): String {
        val activities = activityDao.getAllActivities().first()
        val weights = weightDao.getAllWeightEntries().first()
        val achievements = achievementDao.getAllAchievements().first()

        val rootObj = JSONObject()
        rootObj.put("exportVersion", 1)
        rootObj.put("exportedAt", System.currentTimeMillis())

        val actArray = JSONArray()
        for (a in activities) {
            val item = JSONObject()
            item.put("date", a.date)
            item.put("steps", a.steps)
            item.put("distanceKm", a.distanceKm)
            item.put("calories", a.calories)
            item.put("activeMinutes", a.activeMinutes)
            item.put("goalSteps", a.goalSteps)
            item.put("isGoalCompleted", a.isGoalCompleted)
            actArray.put(item)
        }
        rootObj.put("activities", actArray)

        val weightArray = JSONArray()
        for (w in weights) {
            val item = JSONObject()
            item.put("date", w.date)
            item.put("weightKg", w.weightKg)
            item.put("notes", w.notes)
            weightArray.put(item)
        }
        rootObj.put("weights", weightArray)

        val achArray = JSONArray()
        for (ach in achievements) {
            if (ach.isUnlocked) {
                val item = JSONObject()
                item.put("id", ach.id)
                item.put("unlockedAt", ach.unlockedAt ?: 0L)
                achArray.put(item)
            }
        }
        rootObj.put("achievements", achArray)

        return rootObj.toString(2)
    }

    suspend fun exportDataAsCsv(): String {
        val activities = activityDao.getAllActivities().first()
        val sb = StringBuilder()
        sb.append("Date,Steps,Distance_KM,Calories_KCal,Active_Minutes,Goal_Steps,Goal_Completed\n")
        for (a in activities) {
            sb.append("${a.date},${a.steps},${String.format("%.2f", a.distanceKm)},${a.calories},${a.activeMinutes},${a.goalSteps},${a.isGoalCompleted}\n")
        }
        return sb.toString()
    }

    suspend fun restoreDataFromJson(jsonStr: String): Boolean {
        return try {
            val root = JSONObject(jsonStr)
            if (root.has("activities")) {
                val actArray = root.getJSONArray("activities")
                for (i in 0 until actArray.length()) {
                    val obj = actArray.getJSONObject(i)
                    val date = obj.getString("date")
                    val steps = obj.getInt("steps")
                    val distanceKm = obj.getDouble("distanceKm").toFloat()
                    val calories = obj.getInt("calories")
                    val activeMin = obj.getInt("activeMinutes")
                    val goal = obj.optInt("goalSteps", 10000)
                    val completed = obj.optBoolean("isGoalCompleted", steps >= goal)

                    activityDao.insertOrUpdate(
                        DailyActivityEntity(
                            date = date,
                            steps = steps,
                            distanceKm = distanceKm,
                            calories = calories,
                            activeMinutes = activeMin,
                            goalSteps = goal,
                            isGoalCompleted = completed
                        )
                    )
                }
            }
            if (root.has("weights")) {
                val weightArray = root.getJSONArray("weights")
                for (i in 0 until weightArray.length()) {
                    val obj = weightArray.getJSONObject(i)
                    weightDao.insertWeight(
                        WeightEntity(
                            date = obj.getString("date"),
                            weightKg = obj.getDouble("weightKg").toFloat(),
                            notes = obj.optString("notes", "")
                        )
                    )
                }
            }
            if (root.has("achievements")) {
                val achArray = root.getJSONArray("achievements")
                for (i in 0 until achArray.length()) {
                    val obj = achArray.getJSONObject(i)
                    val id = obj.getString("id")
                    val unlockedAt = obj.optLong("unlockedAt", System.currentTimeMillis())
                    achievementDao.unlockAchievement(id, unlockedAt)
                }
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun clearAllLocalData() {
        activityDao.deleteAll()
        waterDao.deleteAll()
        weightDao.deleteAll()
        achievementDao.deleteAll()
        achievementDao.insertAchievements(HealthContentRepository.defaultAchievements)
    }
}
