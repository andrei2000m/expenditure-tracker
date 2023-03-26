package com.andrei2000m.expendituretracker.sql

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromDateString(value: String): LocalDate {
        return LocalDate.parse(value)
    }

    @TypeConverter
    fun dateToDateString(date: LocalDate): String {
        return date.toString()
    }
}