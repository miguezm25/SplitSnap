package com.miguelzapata.splitsnap.domain

import com.miguelzapata.splitsnap.data.local.Gasto
import kotlin.math.min

object CalculadoraBalances {

    private const val TOLERANCIA = 0.01

    fun calcularBalances(miembros: List<String>, gastos: List<Gasto>): Map<String, Double> {
        if (miembros.isEmpty()) return emptyMap()

        val balances = miembros.associateWith { 0.0 }.toMutableMap()

        for (gasto in gastos) {
            val parte = gasto.monto / miembros.size

            balances[gasto.pagadoPor] = (balances[gasto.pagadoPor] ?: 0.0) + gasto.monto

            for (miembro in miembros) {
                balances[miembro] = (balances[miembro] ?: 0.0) - parte
            }
        }
        return balances
    }

    fun simplificarDeudas(balances: Map<String, Double>): List<Deuda> {
        val deudores = balances
            .filter { it.value < -TOLERANCIA }
            .map { it.key to -it.value }
            .sortedByDescending { it.second }
            .toMutableList()

        val acreedores = balances
            .filter { it.value > TOLERANCIA }
            .map { it.key to it.value }
            .sortedByDescending { it.second }
            .toMutableList()

        val deudas = mutableListOf<Deuda>()
        var i = 0
        var j = 0

        while (i < deudores.size && j < acreedores.size) {
            val (deudor, debe) = deudores[i]
            val (acreedor, recibe) = acreedores[j]
            val monto = min(debe, recibe)

            deudas.add(Deuda(deudor, acreedor, monto))

            deudores[i] = deudor to (debe - monto)
            acreedores[j] = acreedor to (recibe - monto)

            if (deudores[i].second < TOLERANCIA) i++
            if (acreedores[j].second < TOLERANCIA) j++
        }
        return deudas
    }

    fun calcularDeudas(miembros: List<String>, gastos: List<Gasto>): List<Deuda> {
        return simplificarDeudas(calcularBalances(miembros, gastos))
    }
}