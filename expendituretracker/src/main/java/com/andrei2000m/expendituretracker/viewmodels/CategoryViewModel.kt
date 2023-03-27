package com.andrei2000m.expendituretracker.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrei2000m.expendituretracker.sql.Category
import com.andrei2000m.expendituretracker.sql.ExpenditureDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    private val db: ExpenditureDb
) : ViewModel() {

    val categoryName = MutableLiveData<String>()

    fun onSubmit() {
        val categoryDao = db.categoryDao()
        viewModelScope.launch(Dispatchers.IO) {
            categoryName.value?.let {
                val newCategory = Category(name = it)
                val result = categoryDao.insertCategory(newCategory)
                Log.i("category", categoryDao.getAll().toString())
                //TODO: Add popup (Toast?) if result = -1
            }
            //TODO: Add branch in case of null
        }
    }
}