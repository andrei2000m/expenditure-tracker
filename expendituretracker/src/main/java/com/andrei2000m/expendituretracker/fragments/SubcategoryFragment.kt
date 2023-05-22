package com.andrei2000m.expendituretracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.andrei2000m.expendituretracker.R
import com.andrei2000m.expendituretracker.databinding.SubcategoryFragmentBinding
import com.andrei2000m.expendituretracker.sql.Category
import com.andrei2000m.expendituretracker.sql.ExpenditureDb
import com.andrei2000m.expendituretracker.viewmodels.SubcategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubcategoryFragment : Fragment() {

    @Inject
    lateinit var db: ExpenditureDb

    lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: SubcategoryFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.subcategory_fragment, container, false)
        val subcategoryModel = activity?.let { SubcategoryViewModel(db, it.application) }

        binding.lifecycleOwner = this
        binding.subcategoryViewModel = subcategoryModel

        initialiseSpinnerData(binding)

        subcategoryModel?.toastMessage?.observe(viewLifecycleOwner) {
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

    private fun initialiseSpinnerData(binding: SubcategoryFragmentBinding) {
        spinner = binding.categorySpinner
        context?.let {
            lifecycleScope.launch(Dispatchers.IO) {
                val adapter = ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item,
                    db.categoryDao().getAll()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }
        spinner.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                binding.subcategoryViewModel?.category?.value = parent.getItemAtPosition(position) as Category
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }
}