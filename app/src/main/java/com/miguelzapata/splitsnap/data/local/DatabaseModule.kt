package com.miguelzapata.splitsnap.data.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun proveerDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "splitsnap-db"
        ).build()
    }

    @Provides
    fun proveerGrupoDao(database: AppDatabase): GrupoDao {
        return database.grupoDao()
    }

    @Provides
    fun proveerGastoDao(database: AppDatabase): GastoDao {
        return database.gastoDao()
    }
}