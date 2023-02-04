package com.habidev.bookdb

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.habidev.bookdb.Activity.MainActivity
import com.habidev.bookdb.Fragment.BookListFragment
import com.habidev.bookdb.Fragment.CameraFragment

class ViewPagerAdapter(
    mainActivity: MainActivity
): FragmentStateAdapter(mainActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            CameraFragment()
        } else {
            BookListFragment()
        }
    }
}