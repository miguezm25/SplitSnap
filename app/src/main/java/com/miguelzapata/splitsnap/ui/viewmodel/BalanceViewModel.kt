package com.miguelzapata.splitsnap.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miguelzapata.splitsnap.data.local.Grupo
import com.miguelzapata.splitsnap.data.repository.GastoRepository
import com.miguelzapata.splitsnap.data.repository.GrupoRepository
import com.miguelzapata.splitsnap.domain.CalculadoraBalances
import com.miguelzapata.splitsnap.domain.Deuda
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BalanceUiState(
    val balances: Map<String, Double> = emptyMap(),
    val deudas: List<Deuda> = emptyList()
)

@HiltViewModel
class BalanceViewModel @Inject constructor(
    gastoRepository: GastoRepository,
    grupoRepository: GrupoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val grupoId: Long = checkNotNull(savedStateHandle["grupoId"])

    private val _grupo = MutableStateFlow<Grupo?>(null)
    val grupo: StateFlow<Grupo?> = _grupo

    init {
        viewModelScope.launch {
            _grupo.value = grupoRepository.obtenerGrupoPorId(grupoId)
        }
    }

    val uiState: StateFlow<BalanceUiState> = combine(
        _grupo,
        gastoRepository.obtenerGastosDeGrupo(grupoId)
    ) { grupo, gastos ->
        val balances = CalculadoraBalances.calcularBalances(grupo?.miembros ?: emptyList(), gastos)
        BalanceUiState(
            balances = balances,
            deudas = CalculadoraBalances.simplificarDeudas(balances)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BalanceUiState()
    )
}