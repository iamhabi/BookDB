package com.habidev.bookdb.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.habidev.bookdb.activity.MainActivity
import com.habidev.bookdb.fragment.BookListFragment
import com.habidev.bookdb.fragment.CameraFragment
import com.habidev.bookdb.fragment.SearchFragment

class MainViewPagerAdapter(
    mainActivity: MainActivity
): FragmentStateAdapter(mainActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                CameraFragment()
            }
            1 -> {
                BookListFragment()
            }
            else -> {
                SearchFragment()
            }
        }
    }
}