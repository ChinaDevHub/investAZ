package com.example.investaz.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.investaz.data.local.dao.PriceDao
import com.example.investaz.data.local.entity.PriceEntity

@Database(entities = [PriceEntity::class], version = 1, exportSchema = false)
abstract class PriceDatabase : RoomDatabase() {

    abstract fun priceDao(): PriceDao

    companion object {
        private const val NAME = "investaz.db"

        // The table is a refillable cache, so dropping it on schema changes is safe.
        fun create(context: Context): PriceDatabase =
            Room.databaseBuilder(context.applicationContext, PriceDatabase::class.java, NAME)
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
    }
}
