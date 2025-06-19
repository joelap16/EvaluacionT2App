package com.cibertec.evaluaciont2app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cibertec.evaluaciont2app.data.dao.PersonaDao
import com.cibertec.evaluaciont2app.data.model.Persona

@Database(entities = [Persona::class], version = 3)
abstract class AppDatabase: RoomDatabase() {

    abstract fun personaDao(): PersonaDao

    companion object{

    }
}