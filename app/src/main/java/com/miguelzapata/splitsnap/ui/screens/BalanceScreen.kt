package com.miguelzapata.splitsnap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.miguelzapata.splitsnap.ui.viewmodel.BalanceViewModel
import kotlin.math.abs

private fun formatearMonto(monto: Double): String = "$" + "%,.2f".format(monto)

@Composable
fun BalanceScreen(
    onVolverClick: () -> Unit,
    viewModel: BalanceViewModel = hiltViewModel()
) {
    val grupo by viewModel.grupo.collectAsState()
    val estado by viewModel.uiState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(onClick = onVolverClick) {
                Text("← Volver al grupo")
            }

            Text(text = "Balance de ${grupo?.nombre ?: "..."}")

            Text(text = "Quién debe a quién")
            if (estado.deudas.isEmpty()) {
                Text(text = "Todos están a mano")
            } else {
                estado.deudas.forEach { deuda ->
                    Text(
                        text = "${deuda.deudor} le debe ${formatearMonto(deuda.monto)} a ${deuda.acreedor}"
                    )
                }
            }

            Text(text = "Balance por persona")
            estado.balances.forEach { (nombre, balance) ->
                val signo = if (balance >= 0) "+" else "-"
                Text(text = "$nombre: $signo${formatearMonto(abs(balance))}")
            }
        }
    }
}