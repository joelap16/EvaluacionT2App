package com.cibertec.evaluaciont2app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cibertec.evaluaciont2app.data.model.Persona
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(persona: Persona)

    @Update
    suspend fun update(persona: Persona)

    @Delete
    suspend fun delete(persona: Persona)

    @Query("SELECT * FROM tbl_persona WHERE id = :id")
    suspend fun getPersonaById(id: Int): Persona?

    @Query("SELECT * FROM tbl_persona")
    fun getAllPersonas(): Flow<List<Persona>>

    @Query("SELECT * FROM tbl_persona WHERE " +
            "(:query IS NULL OR :query = '' OR nombres LIKE '%' || :query || '%' OR apellidos LIKE '%' || :query || '%' OR numero_dni LIKE '%' || :query || '%')")
    fun findPersonas(query: String): Flow<List<Persona>>

}