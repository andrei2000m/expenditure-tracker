package com.andrei2000m.expendituretracker.sql

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey val categoryId: Int,
    val name: String
)

@Dao
interface CategoryDao {

}