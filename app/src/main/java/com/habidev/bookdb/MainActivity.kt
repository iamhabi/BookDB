package com.habidev.bookdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.habidev.bookdb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initViewPager()
        initBottomNav()
    }

    private fun initViewPager() {
        val adapter = ViewPagerAdapter(this)

        viewPager = viewBinding.viewPager

        viewPager.adapter = adapter

        viewPager.setCurrentItem(1, false)

        viewPager.registerOnPageChangeCallback(object: OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when (position) {
                    0 -> viewBinding.bottomNav.selectedItemId = R.id.btn_camera_frag
                    1 -> viewBinding.bottomNav.selectedItemId = R.id.btn_book_list_frag
                }
            }
        })
    }

    private fun initBottomNav() {
        viewBinding.bottomNav.selectedItemId = R.id.btn_book_list_frag

        viewBinding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.btn_camera_frag -> changeToCameraFragment()
                R.id.btn_book_list_frag -> changeToBookListFragment()
            }

            true
        }
    }

    private fun changeToCameraFragment() {
        viewPager.setCurrentItem(0, true)
    }

    private fun changeToBookListFragment() {
        viewPager.setCurrentItem(1, true)
    }
}