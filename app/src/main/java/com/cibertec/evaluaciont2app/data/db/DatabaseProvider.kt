package com.cibertec.evaluaciont2app.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.cibertec.evaluaciont2app.data.db.AppDatabase

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase{
        return INSTANCE ?: synchronized(this){
            val instance = databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "padron_personas_db"
                )
                .fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            instance
        }
    }
}