package com.andrei2000m.expendituretracker.sql

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Category::class, Subcategory::class, Transaction::class], version = 1)
@TypeConverters(Converters::class)
abstract class ExpenditureDb : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun subcategoryDao(): SubcategoryDao
    abstract fun transactionDao(): TransactionDao
}