package com.miguelzapata.splitsnap.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miguelzapata.splitsnap.data.local.Gasto
import com.miguelzapata.splitsnap.data.repository.GastoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GastoViewModel @Inject constructor(
    private val gastoRepository: GastoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val grupoId: Long = checkNotNull(savedStateHandle["grupoId"])

    val gastos: StateFlow<List<Gasto>> = gastoRepository.obtenerGastosDeGrupo(grupoId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun agregarGasto(monto: Double, descripcion: String, categoria: String, pagadoPor: String) {
        viewModelScope.launch {
            gastoRepository.agregarGasto(
                Gasto(
                    grupoId = grupoId,
                    monto = monto,
                    descripcion = descripcion,
                    categoria = categoria,
                    pagadoPor = pagadoPor,
                    fecha = System.currentTimeMillis()
                )
            )
        }
    }
}