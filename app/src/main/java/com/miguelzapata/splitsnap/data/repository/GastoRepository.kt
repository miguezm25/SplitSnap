package com.miguelzapata.splitsnap.data.repository

import com.miguelzapata.splitsnap.data.local.Gasto
import com.miguelzapata.splitsnap.data.local.GastoDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GastoRepository @Inject constructor(
    private val gastoDao: GastoDao
) {
    fun obtenerGastosDeGrupo(grupoId: Long): Flow<List<Gasto>> {
        return gastoDao.obtenerPorGrupo(grupoId)
    }

    suspend fun agregarGasto(gasto: Gasto): Long {
        return gastoDao.insertar(gasto)
    }

    suspend fun actualizarGasto(gasto: Gasto) {
        gastoDao.actualizar(gasto)
    }

    suspend fun eliminarGasto(gasto: Gasto) {
        gastoDao.eliminar(gasto)
    }
}