package com.habidev.bookdb.Activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.habidev.bookdb.*
import com.habidev.bookdb.Adapter.MainViewPagerAdapter
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
        val adapter = MainViewPagerAdapter(this)

        viewPager = viewBinding.viewPager

        viewPager.adapter = adapter

        TabLayoutMediator(viewBinding.viewPagerTabLayout, viewPager) { tab , position ->
            when (position) {
                0 -> {
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.camera)
                    tab.text = "Camera"
                }
                1 -> {
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.collections_bookmark)
                    tab.text = "List"
                }
                2 -> {
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.search)
                    tab.text = "Search"
                }
            }
        }.attach()
    }
}