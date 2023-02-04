package com.habidev.bookdb.Activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.habidev.bookdb.*
import com.habidev.bookdb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((application as BooksApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        bookViewModel.create()

        initViewPager()
        initBottomNav()
    }

    private fun initViewPager() {
        val adapter = ViewPagerAdapter(this)

        viewPager = viewBinding.viewPager

        viewPager.adapter = adapter

//        viewPager.setCurrentItem(1, false)

        viewPager.registerOnPageChangeCallback(object: OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when (position) {
                    0 -> changeBottomNavToCamera()
                    1 -> changeBottomNavToBookList()
                }
            }
        })
    }

    private fun initBottomNav() {
        changeBottomNavToBookList()

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

    private fun changeBottomNavToCamera() {
        viewBinding.bottomNav.selectedItemId = R.id.btn_camera_frag
    }

    private fun changeBottomNavToBookList() {
        viewBinding.bottomNav.selectedItemId = R.id.btn_book_list_frag
    }
}