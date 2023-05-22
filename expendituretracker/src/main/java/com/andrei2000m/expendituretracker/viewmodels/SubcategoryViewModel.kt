package com.andrei2000m.expendituretracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.andrei2000m.expendituretracker.sql.Category
import com.andrei2000m.expendituretracker.sql.ExpenditureDb
import com.andrei2000m.expendituretracker.sql.Subcategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SubcategoryViewModel constructor(
    db: ExpenditureDb, application: Application
) : AndroidViewModel(application) {

    private val subcategoryDao = db.subcategoryDao()
    val subcategories = subcategoryDao.getAllLive().map {
        it.joinToString(separator = "\n")
    }

    val subcategoryName = MutableLiveData<String>()
    val category = MutableLiveData<Category>()

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun onSubmit() {
        viewModelScope.launch(Dispatchers.IO) {
            subcategoryName.value?.let {subcategory ->
                category.value?.let {category ->
                    val newSubcategory = Subcategory(name = subcategory, categoryId = category.categoryId)
                    val result = subcategoryDao.insertSubcategory(newSubcategory)

                    if (result == -1L) {
                        _toastMessage.postValue("Subcategory $subcategory with category ${category.name} already exists")
                    }
                }
            }
            //TODO: Add branch in case of null
        }
    }
}