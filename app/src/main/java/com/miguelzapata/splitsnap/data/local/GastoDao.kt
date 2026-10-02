package com.miguelzapata.splitsnap.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {

    @Insert
    suspend fun insertar(gasto: Gasto): Long

    @Update
    suspend fun actualizar(gasto: Gasto)

    @Delete
    suspend fun eliminar(gasto: Gasto)

    @Query("SELECT * FROM gastos WHERE grupoId = :grupoId ORDER BY fecha DESC")
    fun obtenerPorGrupo(grupoId: Long): Flow<List<Gasto>>
}