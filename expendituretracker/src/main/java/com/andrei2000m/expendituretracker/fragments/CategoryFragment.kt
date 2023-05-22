package com.andrei2000m.expendituretracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.andrei2000m.expendituretracker.R
import com.andrei2000m.expendituretracker.databinding.CategoryFragmentBinding
import com.andrei2000m.expendituretracker.sql.ExpenditureDb
import com.andrei2000m.expendituretracker.viewmodels.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    @Inject
    lateinit var db: ExpenditureDb

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: CategoryFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.category_fragment, container, false)
        val categoryModel = activity?.let { CategoryViewModel(db, it.application) }

        binding.lifecycleOwner = this
        binding.categoryViewModel = categoryModel

        categoryModel?.toastMessage?.observe(viewLifecycleOwner) {
            activity?.let { activity ->
                Toast.makeText(
                    activity.application.applicationContext,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return binding.root
    }
}