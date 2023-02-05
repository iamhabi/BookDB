package com.habidev.bookdb

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.habidev.bookdb.Activity.MainActivity
import com.habidev.bookdb.Fragment.BookListFragment
import com.habidev.bookdb.Fragment.CameraFragment
import com.habidev.bookdb.Fragment.SearchFragment

class ViewPagerAdapter(
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