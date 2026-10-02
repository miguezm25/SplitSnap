package com.miguelzapata.splitsnap.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grupos")
data class Grupo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val miembros: List<String>
)