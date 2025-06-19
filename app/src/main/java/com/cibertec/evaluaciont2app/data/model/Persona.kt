package com.cibertec.evaluaciont2app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_persona")
data class Persona(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "nombres")
    val nombres: String,
    @ColumnInfo(name = "apellidos")
    val apellidos: String,
    @ColumnInfo(name = "numero_dni")
    val numeroDNI: String,
    @ColumnInfo(name = "direccion")
    val direccion: String,
    @ColumnInfo(name = "telefono")
    val telefono: String,
    @ColumnInfo(name = "distrito")
    val distrito: String?,

)
