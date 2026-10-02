package com.miguelzapata.splitsnap.data.local

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromListaString(lista: List<String>): String {
        return lista.joinToString(separator = ",")
    }

    @TypeConverter
    fun toListaString(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split(",")
    }
}