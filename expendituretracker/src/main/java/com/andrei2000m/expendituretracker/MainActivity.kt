package com.andrei2000m.expendituretracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.andrei2000m.expendituretracker.databinding.ActivityMainBinding
import com.andrei2000m.expendituretracker.fragments.CategoryFragment
import com.andrei2000m.expendituretracker.fragments.SubcategoryFragment
import com.andrei2000m.expendituretracker.sql.ExpenditureDb
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//TODO: Make list not string-typed (enum?)
val fragmentList = listOf("category", "subcategory")

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var db: ExpenditureDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this

        val stateAdapter = ViewStateAdapter(supportFragmentManager, lifecycle)
        val pager: ViewPager2 = findViewById(R.id.pager)
        pager.adapter = stateAdapter

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = fragmentList[position]
        }
    }
}

private class ViewStateAdapter constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (fragmentList[position]) {
            "category" -> CategoryFragment()
            "subcategory" -> SubcategoryFragment()
            else -> throw Exception("Fragment type not implemented")
        }
    }

    fun getTitle(position: Int): String {
        return fragmentList[position]
    }

}