package com.andrei2000m.expendituretracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.andrei2000m.expendituretracker.databinding.ActivityMainBinding
import com.andrei2000m.expendituretracker.sql.ExpenditureDb
import com.andrei2000m.expendituretracker.viewmodels.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var db: ExpenditureDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val categoryModel = CategoryViewModel(db)

        binding.lifecycleOwner = this
        binding.categoryViewModel = categoryModel
    }
}