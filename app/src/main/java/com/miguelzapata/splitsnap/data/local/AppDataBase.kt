package com.miguelzapata.splitsnap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Grupo::class, Gasto::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun grupoDao(): GrupoDao
    abstract fun gastoDao(): GastoDao
}