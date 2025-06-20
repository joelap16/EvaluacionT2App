package com.cibertec.evaluaciont2app.repository

import android.content.Context
import com.cibertec.evaluaciont2app.data.db.DatabaseProvider
import com.cibertec.evaluaciont2app.data.model.Persona
import kotlinx.coroutines.flow.Flow

class PersonaRepository (private val context: Context) {
    private val personaDao = DatabaseProvider.getDatabase(context).personaDao()

    fun getAllPersonas(): Flow<List<Persona>>{
        return personaDao.getAllPersonas()
    }

    fun findPersonas(query: String): Flow<List<Persona>> {
        return personaDao.findPersonas(query)
    }

    suspend fun getPersonaById(id: Int): Persona? {
        return personaDao.getPersonaById(id)
    }

    suspend fun insertPersona(persona: Persona) {
        personaDao.insert(persona)
    }

    suspend fun updatePersona(persona: Persona) {
        personaDao.update(persona)
    }

    suspend fun deletePersona(persona: Persona) {
        personaDao.delete(persona)
    }
}