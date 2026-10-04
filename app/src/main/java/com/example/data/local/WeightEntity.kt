package com.example.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "weight_entries")
data class WeightEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val weightKg: Float,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface WeightDao {
    @Query("SELECT * FROM weight_entries ORDER BY date DESC, timestamp DESC")
    fun getAllWeightEntries(): Flow<List<WeightEntity>>

    @Query("SELECT * FROM weight_entries ORDER BY date DESC, timestamp DESC LIMIT 1")
    fun getLatestWeight(): Flow<WeightEntity?>

    @Query("SELECT * FROM weight_entries ORDER BY date DESC, timestamp DESC LIMIT 1")
    suspend fun getLatestWeightOnce(): WeightEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(entry: WeightEntity)

    @Query("DELETE FROM weight_entries WHERE id = :id")
    suspend fun deleteWeight(id: Long)

    @Query("DELETE FROM weight_entries")
    suspend fun deleteAll()
}
