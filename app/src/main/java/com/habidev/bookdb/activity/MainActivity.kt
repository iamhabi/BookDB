package com.habidev.bookdb.activity

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
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
    companion object {
        private const val TAG = "BookDBMainAct"
    }

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((application as BooksApplication).repository)
    }

    private val bookListFragment = BookListFragment()
    private val searchFragment = SearchFragment()

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        bookViewModel.create()

        initBookList()
        initViewListener()
    }

    private fun initBookList() {
        supportFragmentManager.beginTransaction()
            .add(viewBinding.frameLayoutBookList.id, bookListFragment)
            .commit()
    }

    private fun initViewListener() {
        viewBinding.btnSearch.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(viewBinding.frameLayoutFull.id, searchFragment)
                .addToBackStack(null)
                .commit()
        }

        viewBinding.btnMore.setOnClickListener {

        }
    }
}