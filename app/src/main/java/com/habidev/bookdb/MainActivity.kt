package com.habidev.bookdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.habidev.bookdb.database.BookDao
import com.habidev.bookdb.database.BookDatabase
import com.habidev.bookdb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2

    private lateinit var bookDao: BookDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initViewPager()
        initBottomNav()

        initBookDao()
    }

    private fun initBookDao() {
        val database = Room.databaseBuilder(
            applicationContext,
            BookDatabase::class.java, "books"
        ).build()

        bookDao = database.bookDao()
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