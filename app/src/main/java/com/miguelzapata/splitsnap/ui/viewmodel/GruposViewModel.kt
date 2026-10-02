package com.miguelzapata.splitsnap.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miguelzapata.splitsnap.data.local.Grupo
import com.miguelzapata.splitsnap.data.repository.GrupoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GruposViewModel @Inject constructor(
    private val grupoRepository: GrupoRepository
) : ViewModel() {

    val grupos: StateFlow<List<Grupo>> = grupoRepository.obtenerGrupos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun crearGrupo(nombre: String, miembros: List<String>) {
        viewModelScope.launch {
            grupoRepository.crearGrupo(
                Grupo(nombre = nombre, miembros = miembros)
            )
        }
    }
}