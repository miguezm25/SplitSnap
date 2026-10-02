package com.miguelzapata.splitsnap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.miguelzapata.splitsnap.ui.viewmodel.GastoViewModel

@Composable
fun DetalleScreen(
    onVolverClick: () -> Unit,
    viewModel: GastoViewModel = hiltViewModel()
) {
    val gastos by viewModel.gastos.collectAsState()

    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var pagadoPor by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            TextButton(onClick = onVolverClick) {
                Text("← Volver a Grupos")
            }

            Text(text = "Agregar gasto")

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

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pagadoPor,
                onValueChange = { pagadoPor = it },
                label = { Text("Pagado por") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val montoDouble = monto.toDoubleOrNull()
                    if (montoDouble != null && descripcion.isNotBlank()) {
                        viewModel.agregarGasto(montoDouble, descripcion, categoria, pagadoPor)
                        monto = ""
                        descripcion = ""
                        categoria = ""
                        pagadoPor = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar gasto")
            }

            Text(text = "Gastos del grupo")

            LazyColumn {
                items(gastos) { gasto ->
                    Text(
                        text = "${gasto.descripcion}: $${gasto.monto} (pagó ${gasto.pagadoPor})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}