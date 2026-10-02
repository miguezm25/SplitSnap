package com.miguelzapata.splitsnap.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gastos")
data class Gasto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val grupoId: Long,
    val monto: Double,
    val descripcion: String,
    val categoria: String,
    val pagadoPor: String,
    val fecha: Long
)