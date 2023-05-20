package com.andrei2000m.expendituretracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.andrei2000m.expendituretracker.sql.Category
import com.andrei2000m.expendituretracker.sql.ExpenditureDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    db: ExpenditureDb, application: Application
) : AndroidViewModel(application) {

    private val categoryDao = db.categoryDao()
    val categories = categoryDao.getAllLive().map {
        it.joinToString(separator = "\n")
    }

    val categoryName = MutableLiveData<String>()

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun onSubmit() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryName.value?.let {
                val newCategory = Category(name = it)
                val result = categoryDao.insertCategory(newCategory)

                if (result == -1L) {
                    _toastMessage.postValue("Category $it already exists")
                }
            }
            //TODO: Add branch in case of null
        }
    }
}