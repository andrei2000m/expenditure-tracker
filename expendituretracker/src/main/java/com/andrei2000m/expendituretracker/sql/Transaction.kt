package com.andrei2000m.expendituretracker.sql

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(foreignKeys = [ForeignKey(entity = Subcategory::class,
    parentColumns = ["subcategoryId"],
    childColumns = ["subcategoryId"],
    onDelete = ForeignKey.CASCADE)]
)
data class Transaction(
    @PrimaryKey val transactionId: Int,
    val date: LocalDate,
    val subcategoryId: Int,
    val value: Double,
    val isDebitTransaction: Boolean
)

@Dao
interface TransactionDao {

}