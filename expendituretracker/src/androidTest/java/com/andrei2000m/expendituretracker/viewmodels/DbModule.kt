package com.andrei2000m.expendituretracker.viewmodels

import android.content.Context
import androidx.room.Room
import com.andrei2000m.expendituretracker.sql.ExpenditureDb
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DbModule::class])
object TestDbModule {

    @Provides
    @Singleton
    fun provideInMemoryDb(@ApplicationContext context: Context): ExpenditureDb {
        return Room.inMemoryDatabaseBuilder(context, ExpenditureDb::class.java)
            .allowMainThreadQueries()
            .build()
    }
}