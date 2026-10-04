package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyActivityDao {
    @Query("SELECT * FROM daily_activity WHERE date = :date LIMIT 1")
    fun getActivityForDate(date: String): Flow<DailyActivityEntity?>

    @Query("SELECT * FROM daily_activity WHERE date = :date LIMIT 1")
    suspend fun getActivityForDateOnce(date: String): DailyActivityEntity?

    @Query("SELECT * FROM daily_activity ORDER BY date DESC")
    fun getAllActivities(): Flow<List<DailyActivityEntity>>

    @Query("SELECT * FROM daily_activity ORDER BY date DESC LIMIT :limit")
    fun getRecentActivities(limit: Int): Flow<List<DailyActivityEntity>>

    @Query("SELECT * FROM daily_activity WHERE date >= :startDate ORDER BY date ASC")
    fun getActivitiesFromDate(startDate: String): Flow<List<DailyActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(activity: DailyActivityEntity)

    @Query("SELECT SUM(steps) FROM daily_activity")
    fun getTotalLifetimeSteps(): Flow<Int?>

    @Query("SELECT SUM(distanceKm) FROM daily_activity")
    fun getTotalLifetimeDistance(): Flow<Float?>

    @Query("SELECT MAX(steps) FROM daily_activity")
    fun getBestStepsDay(): Flow<Int?>

    @Query("DELETE FROM daily_activity")
    suspend fun deleteAll()
}
