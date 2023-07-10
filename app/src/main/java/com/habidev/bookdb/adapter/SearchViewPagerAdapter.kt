package com.habidev.bookdb.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.habidev.bookdb.fragment.SearchDBFragment
import com.habidev.bookdb.fragment.SearchInternetFragment

class SearchViewPagerAdapter(
    fragmentActivity: FragmentActivity
): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SearchDBFragment()
            else -> SearchInternetFragment()
        }
    }
}