package com.miguelzapata.splitsnap.domain

import com.miguelzapata.splitsnap.data.local.Gasto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculadoraBalancesTest {

    private val miembros = listOf("Ana", "Luis", "Sofía")

    private fun gasto(pagadoPor: String, monto: Double) = Gasto(
        grupoId = 1,
        monto = monto,
        descripcion = "test",
        categoria = "Otros",
        pagadoPor = pagadoPor,
        fecha = 0L
    )

    @Test
    fun `un gasto pagado por una persona se divide en partes iguales`() {
        val balances = CalculadoraBalances.calcularBalances(
            miembros, listOf(gasto("Ana", 90.0))
        )

        assertEquals(60.0, balances["Ana"]!!, 0.001)
        assertEquals(-30.0, balances["Luis"]!!, 0.001)
        assertEquals(-30.0, balances["Sofía"]!!, 0.001)
    }

    @Test
    fun `varios gastos combinan los balances`() {
        val gastos = listOf(gasto("Ana", 90.0), gasto("Luis", 30.0))

        val balances = CalculadoraBalances.calcularBalances(miembros, gastos)

        assertEquals(50.0, balances["Ana"]!!, 0.001)
        assertEquals(-10.0, balances["Luis"]!!, 0.001)
        assertEquals(-40.0, balances["Sofía"]!!, 0.001)
    }

    @Test
    fun `la suma de los balances siempre es cero`() {
        val gastos = listOf(gasto("Ana", 100.0), gasto("Luis", 33.33), gasto("Sofía", 7.5))

        val balances = CalculadoraBalances.calcularBalances(miembros, gastos)

        assertEquals(0.0, balances.values.sum(), 0.001)
    }

    @Test
    fun `sin gastos nadie debe nada`() {
        val balances = CalculadoraBalances.calcularBalances(miembros, emptyList())
        val deudas = CalculadoraBalances.calcularDeudas(miembros, emptyList())

        assertTrue(balances.values.all { it == 0.0 })
        assertTrue(deudas.isEmpty())
    }

    @Test
    fun `un grupo sin miembros no falla`() {
        val balances = CalculadoraBalances.calcularBalances(
            emptyList(), listOf(gasto("Ana", 50.0))
        )

        assertTrue(balances.isEmpty())
    }

    @Test
    fun `las deudas resultantes saldan a todos`() {
        val gastos = listOf(gasto("Ana", 90.0), gasto("Luis", 30.0))

        val deudas = CalculadoraBalances.calcularDeudas(miembros, gastos)

        assertEquals(2, deudas.size)

        val deudaSofia = deudas.first { it.deudor == "Sofía" }
        assertEquals("Ana", deudaSofia.acreedor)
        assertEquals(40.0, deudaSofia.monto, 0.01)

        val deudaLuis = deudas.first { it.deudor == "Luis" }
        assertEquals("Ana", deudaLuis.acreedor)
        assertEquals(10.0, deudaLuis.monto, 0.01)
    }

    @Test
    fun `una cadena de deudas se simplifica en un solo pago`() {
        // A le debe a B, y B le debe a C: B queda en cero y A paga directo a C
        val balances = mapOf("A" to -10.0, "B" to 0.0, "C" to 10.0)

        val deudas = CalculadoraBalances.simplificarDeudas(balances)

        assertEquals(1, deudas.size)
        assertEquals("A", deudas[0].deudor)
        assertEquals("C", deudas[0].acreedor)
        assertEquals(10.0, deudas[0].monto, 0.01)
    }

    @Test
    fun `pagos iguales con decimales no generan deudas fantasma`() {
        val gastos = listOf(gasto("Ana", 100.0), gasto("Luis", 100.0), gasto("Sofía", 100.0))

        val deudas = CalculadoraBalances.calcularDeudas(miembros, gastos)

        assertTrue(deudas.isEmpty())
    }

    @Test
    fun `un monto que no se divide exacto reparte bien la deuda`() {
        val deudas = CalculadoraBalances.calcularDeudas(
            miembros, listOf(gasto("Ana", 100.0))
        )

        assertEquals(2, deudas.size)
        assertTrue(deudas.all { it.acreedor == "Ana" })
        assertEquals(33.33, deudas[0].monto, 0.01)
        assertEquals(33.33, deudas[1].monto, 0.01)
    }
}