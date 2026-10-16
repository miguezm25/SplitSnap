package com.miguelzapata.splitsnap.ui

object Rutas {
    const val GRUPOS = "grupos"
    const val DETALLE = "detalle/{grupoId}"
    const val BALANCE = "balance/{grupoId}"

    fun detalleConId(grupoId: Long) = "detalle/$grupoId"
    fun balanceConId(grupoId: Long) = "balance/$grupoId"
}