package com.miguelzapata.splitsnap.data.repository

import com.miguelzapata.splitsnap.data.local.Grupo
import com.miguelzapata.splitsnap.data.local.GrupoDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GrupoRepository @Inject constructor(
    private val grupoDao: GrupoDao
) {
    fun obtenerGrupos(): Flow<List<Grupo>> {
        return grupoDao.obtenerTodos()
    }

    suspend fun obtenerGrupoPorId(id: Long): Grupo? {
        return grupoDao.obtenerPorId(id)
    }

    suspend fun crearGrupo(grupo: Grupo): Long {
        return grupoDao.insertar(grupo)
    }
}