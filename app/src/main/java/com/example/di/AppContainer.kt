package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.database.ShareLoadDatabase
import com.example.repository.ShareLoadRepository

interface AppContainer {
    val shareLoadRepository: ShareLoadRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val database: ShareLoadDatabase by lazy {
        Room.databaseBuilder(
            context,
            ShareLoadDatabase::class.java,
            "shareload_database"
        ).build()
    }

    override val shareLoadRepository: ShareLoadRepository by lazy {
        ShareLoadRepository(database.shareLoadDao())
    }
}
