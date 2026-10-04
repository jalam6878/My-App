package com.example.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "water_intake")
data class WaterIntakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface WaterDao {
    @Query("SELECT * FROM water_intake WHERE date = :date ORDER BY timestamp DESC")
    fun getWaterIntakeForDate(date: String): Flow<List<WaterIntakeEntity>>

    @Query("SELECT COALESCE(SUM(amountMl), 0) FROM water_intake WHERE date = :date")
    fun getTotalWaterForDate(date: String): Flow<Int>

    @Query("SELECT COALESCE(SUM(amountMl), 0) FROM water_intake WHERE date = :date")
    suspend fun getTotalWaterForDateOnce(date: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaterIntake(water: WaterIntakeEntity)

    @Query("DELETE FROM water_intake WHERE id = :id")
    suspend fun deleteWaterIntake(id: Long)

    @Query("DELETE FROM water_intake WHERE date = :date")
    suspend fun clearWaterForDate(date: String)

    @Query("DELETE FROM water_intake")
    suspend fun deleteAll()
}
