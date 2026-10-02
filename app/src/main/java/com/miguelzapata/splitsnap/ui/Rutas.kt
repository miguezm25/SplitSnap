package com.miguelzapata.splitsnap.ui

object Rutas {
    const val GRUPOS = "grupos"
    const val DETALLE = "detalle/{grupoId}"

    fun detalleConId(grupoId: Long) = "detalle/$grupoId"
}