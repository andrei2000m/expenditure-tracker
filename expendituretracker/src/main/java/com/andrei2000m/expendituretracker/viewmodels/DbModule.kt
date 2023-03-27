package com.andrei2000m.expendituretracker.viewmodels

import android.content.Context
import androidx.room.Room
import com.andrei2000m.expendituretracker.sql.ExpenditureDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object DbModule {

    @Provides
    fun provideDbConnection(@ApplicationContext context: Context): ExpenditureDb {
        return Room.databaseBuilder(context, ExpenditureDb::class.java, "tracker.db")
            .build()
    }
}