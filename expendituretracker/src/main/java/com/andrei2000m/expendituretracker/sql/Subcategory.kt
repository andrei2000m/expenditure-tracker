package com.andrei2000m.expendituretracker.sql

import androidx.room.*

@Entity(foreignKeys = [ForeignKey(entity = Category::class,
    parentColumns = ["categoryId"],
    childColumns = ["categoryId"],
    onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["categoryId", "name"], unique = true)]
)
data class Subcategory(
    @PrimaryKey val subcategoryId: Int,
    val categoryId: Int,
    val name: String
)

@Dao
interface SubcategoryDao {

}