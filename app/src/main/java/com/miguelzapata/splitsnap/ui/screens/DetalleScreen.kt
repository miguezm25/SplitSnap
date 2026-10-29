package com.miguelzapata.splitsnap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.miguelzapata.splitsnap.data.local.Gasto
import com.miguelzapata.splitsnap.domain.Categorias
import com.miguelzapata.splitsnap.ui.components.DropdownSelector
import com.miguelzapata.splitsnap.ui.components.EstadoVacio
import com.miguelzapata.splitsnap.ui.components.TarjetaGasto
import com.miguelzapata.splitsnap.ui.components.TarjetaSuave
import com.miguelzapata.splitsnap.ui.viewmodel.GastoViewModel
import kotlinx.coroutines.launch

@Composable
fun DetalleScreen(
    onVolverClick: () -> Unit,
    onBalanceClick: () -> Unit,
    viewModel: GastoViewModel = hiltViewModel()
) {
    val grupo by viewModel.grupo.collectAsState()
    val gastos by viewModel.gastos.collectAsState()
    val miembros = grupo?.miembros ?: emptyList()

    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var pagadoPor by remember { mutableStateOf("") }
    var gastoEnEdicion by remember { mutableStateOf<Gasto?>(null) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    fun limpiarFormulario() {
        monto = ""
        descripcion = ""
        categoria = ""
        pagadoPor = ""
        gastoEnEdicion = null
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver a grupos"
                        )
                    }
                    Text(
                        text = grupo?.nombre ?: "Cargando...",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = miembros.joinToString(", "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                FilledTonalButton(
                    onClick = onBalanceClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver balance y deudas")
                }
            }

            item {
                TarjetaSuave {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (gastoEnEdicion == null) "Nuevo gasto" else "Editando gasto",
                            style = MaterialTheme.typography.titleMedium
                        )

                        OutlinedTextField(
                            value = monto,
                            onValueChange = { monto = it },
                            label = { Text("Monto") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        DropdownSelector(
                            label = "Categoría",
                            opciones = Categorias.lista,
                            seleccionada = categoria,
                            onSeleccion = { categoria = it }
                        )

                        DropdownSelector(
                            label = "Pagado por",
                            opciones = miembros,
                            seleccionada = pagadoPor,
                            onSeleccion = { pagadoPor = it }
                        )

                        Button(
                            onClick = {
                                val montoDouble = monto.toDoubleOrNull()
                                if (montoDouble != null &&
                                    descripcion.isNotBlank() &&
                                    categoria.isNotBlank() &&
                                    pagadoPor.isNotBlank()
                                ) {
                                    val editando = gastoEnEdicion
                                    if (editando == null) {
                                        viewModel.agregarGasto(
                                            montoDouble, descripcion, categoria, pagadoPor
                                        )
                                    } else {
                                        viewModel.actualizarGasto(
                                            editando.copy(
                                                monto = montoDouble,
                                                descripcion = descripcion,
                                                categoria = categoria,
                                                pagadoPor = pagadoPor
                                            )
                                        )
                                    }
                                    limpiarFormulario()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                if (gastoEnEdicion == null) "Agregar gasto" else "Guardar cambios"
                            )
                        }

                        if (gastoEnEdicion != null) {
                            TextButton(
                                onClick = { limpiarFormulario() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Cancelar edición")
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Gastos del grupo",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (gastos.isEmpty()) {
                item {
                    EstadoVacio(
                        icono = Icons.Filled.ShoppingCart,
                        titulo = "Aún no hay gastos",
                        mensaje = "Agrega el primero con el formulario de arriba y aparecerá aquí."
                    )
                }
            } else {
                items(gastos, key = { it.id }) { gasto ->
                    TarjetaGasto(
                        gasto = gasto,
                        onEditar = {
                            gastoEnEdicion = gasto
                            monto = gasto.monto.toString()
                            descripcion = gasto.descripcion
                            categoria = gasto.categoria
                            pagadoPor = gasto.pagadoPor
                            scope.launch { listState.animateScrollToItem(2) }
                        },
                        onEliminar = {
                            if (gastoEnEdicion?.id == gasto.id) limpiarFormulario()
                            viewModel.eliminarGasto(gasto)
                        }
                    )
                }
            }
        }
    }
}