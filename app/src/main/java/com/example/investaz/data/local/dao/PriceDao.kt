package com.example.investaz.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.investaz.data.local.entity.PriceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceDao {

    @Query("SELECT * FROM prices ORDER BY symbol ASC")
    fun observeAll(): Flow<List<PriceEntity>>

    @Upsert
    suspend fun upsertAll(prices: List<PriceEntity>)
}
