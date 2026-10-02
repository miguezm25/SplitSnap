package com.miguelzapata.splitsnap.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoDao {

    @Insert
    suspend fun insertar(grupo: Grupo): Long

    @Query("SELECT * FROM grupos")
    fun obtenerTodos(): Flow<List<Grupo>>

    @Query("SELECT * FROM grupos WHERE id = :grupoId")
    suspend fun obtenerPorId(grupoId: Long): Grupo?
}