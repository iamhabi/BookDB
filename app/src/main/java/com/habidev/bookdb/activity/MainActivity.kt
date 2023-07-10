package com.habidev.bookdb.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.habidev.bookdb.R
import com.habidev.bookdb.adapter.SimpleViewPagerAdapter
import com.habidev.bookdb.database.BookViewModel
import com.habidev.bookdb.database.BookViewModelFactory
import com.habidev.bookdb.database.BooksApplication
import com.habidev.bookdb.databinding.ActivityMainBinding
import com.habidev.bookdb.fragment.BookListFragment
import com.habidev.bookdb.fragment.CameraFragment
import com.habidev.bookdb.fragment.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

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
        val fragments = arrayListOf(
            CameraFragment(),
            BookListFragment(),
            SearchFragment()
        )

        val adapter = SimpleViewPagerAdapter(this, fragments)

        viewBinding.viewPager.adapter = adapter

        TabLayoutMediator(viewBinding.viewPagerTabLayout, viewBinding.viewPager) { tab, position ->
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
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.cloud)
                    tab.text = "Internet"
                }
            }
        }.attach()
    }
}