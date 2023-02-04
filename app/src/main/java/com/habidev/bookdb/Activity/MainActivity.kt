package com.habidev.bookdb.Activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.habidev.bookdb.BookViewModel
import com.habidev.bookdb.BookViewModelFactory
import com.habidev.bookdb.BooksApplication
import com.habidev.bookdb.ViewPagerAdapter
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
    }

    private fun initViewPager() {
        val adapter = ViewPagerAdapter(this)

        viewPager = viewBinding.viewPager

        viewPager.adapter = adapter

        TabLayoutMediator(viewBinding.viewPagerTabLayout, viewPager) { tab , position ->
            when (position) {
                0 -> tab.text = "Camera"
                1 -> tab.text = "List"
            }
        }.attach()
    }
}