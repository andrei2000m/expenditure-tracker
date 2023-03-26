package com.andrei2000m.expendituretracker.sql

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.MapInfo
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import java.time.LocalDate

@Entity(
    tableName = "transactions",
    foreignKeys = [ForeignKey(
        entity = Subcategory::class,
        parentColumns = ["subcategoryId"],
        childColumns = ["subcategoryId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val transactionId: Int = 0,
    val date: LocalDate,
    val subcategoryId: Int,
    val value: Double,
    val isDebitTransaction: Boolean
)

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTransaction(transaction: Transaction): Long

    @Query("SELECT * FROM transactions")
    fun getAll(): List<Transaction>

    @MapInfo(keyColumn = "category", valueColumn = "total")
    @Query(
        "SELECT C.name AS category, total(T.value) AS total " +
                "FROM categories AS C " +
                "LEFT JOIN subcategories AS S ON S.categoryId = C.categoryId " +
                "LEFT JOIN transactions AS T ON T.subcategoryId = S.subcategoryId " +
                "GROUP BY C.name"
    )
    fun calculateTotalValuesPerCategory(): Map<String, Double>
}