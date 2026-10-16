package com.miguelzapata.splitsnap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.miguelzapata.splitsnap.data.local.Gasto
import com.miguelzapata.splitsnap.domain.Categorias
import com.miguelzapata.splitsnap.ui.components.DropdownSelector
import com.miguelzapata.splitsnap.ui.viewmodel.GastoViewModel

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

    fun limpiarFormulario() {
        monto = ""
        descripcion = ""
        categoria = ""
        pagadoPor = ""
        gastoEnEdicion = null
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            TextButton(onClick = onVolverClick) {
                Text("← Volver a Grupos")
            }

            Text(text = grupo?.nombre ?: "Cargando...")
            Text(text = "Miembros: ${miembros.joinToString(", ")}")

            Button(
                onClick = onBalanceClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver balance y deudas")
            }

            Text(text = if (gastoEnEdicion == null) "Agregar gasto" else "Editando gasto")

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
                            viewModel.agregarGasto(montoDouble, descripcion, categoria, pagadoPor)
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
                Text(if (gastoEnEdicion == null) "Agregar gasto" else "Guardar cambios")
            }

            if (gastoEnEdicion != null) {
                TextButton(
                    onClick = { limpiarFormulario() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar edición")
                }
            }

            Text(text = "Gastos del grupo")

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(gastos) { gasto ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${gasto.descripcion}: $${gasto.monto} (${gasto.categoria} · pagó ${gasto.pagadoPor})",
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = {
                            gastoEnEdicion = gasto
                            monto = gasto.monto.toString()
                            descripcion = gasto.descripcion
                            categoria = gasto.categoria
                            pagadoPor = gasto.pagadoPor
                        }) {
                            Text("Editar")
                        }
                        TextButton(onClick = {
                            if (gastoEnEdicion?.id == gasto.id) limpiarFormulario()
                            viewModel.eliminarGasto(gasto)
                        }) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}