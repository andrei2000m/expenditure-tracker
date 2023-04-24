package com.andrei2000m.expendituretracker.viewmodels

import android.content.Context
import androidx.room.Room
import com.andrei2000m.expendituretracker.sql.ExpenditureDb
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(components = [ActivityComponent::class], replaces = [DbModule::class])
object TestDbModule {

    @Provides
    fun provideInMemoryDb(@ApplicationContext context: Context): ExpenditureDb {
        return Room.inMemoryDatabaseBuilder(context, ExpenditureDb::class.java)
            .allowMainThreadQueries()
            .build()
    }
}