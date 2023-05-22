package com.andrei2000m.expendituretracker.sql

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(
    tableName = "subcategories",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["categoryId"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["categoryId", "name"], unique = true)]
)
data class Subcategory(
    @PrimaryKey(autoGenerate = true) val subcategoryId: Int = 0,
    val categoryId: Int,
    val name: String
)

@Dao
interface SubcategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSubcategory(subcategory: Subcategory): Long

    @Query("SELECT * FROM subcategories")
    fun getAllLive(): LiveData<List<Subcategory>>

    @Query("SELECT * FROM subcategories")
    fun getAll(): List<Subcategory>
}