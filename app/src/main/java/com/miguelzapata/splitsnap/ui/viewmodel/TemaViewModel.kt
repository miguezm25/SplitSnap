package com.miguelzapata.splitsnap.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miguelzapata.splitsnap.data.repository.TemaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemaViewModel @Inject constructor(
    private val temaRepository: TemaRepository
) : ViewModel() {

    val modoOscuro: StateFlow<Boolean?> = temaRepository.modoOscuro
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun cambiarModo(oscuro: Boolean) {
        viewModelScope.launch {
            temaRepository.guardarModoOscuro(oscuro)
        }
    }
}