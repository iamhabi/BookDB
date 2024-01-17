package com.habidev.bookdb.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.database.BookViewModel
import com.habidev.bookdb.database.BookViewModelFactory
import com.habidev.bookdb.database.BooksApplication
import com.habidev.bookdb.databinding.ActivityMainBinding
import com.habidev.bookdb.fragment.BookListFragment
import com.habidev.bookdb.fragment.CameraFragment
import com.habidev.bookdb.fragment.DetailFragment
import com.habidev.bookdb.fragment.GroupListFragment
import com.habidev.bookdb.fragment.ResultFragment
import com.habidev.bookdb.fragment.SearchFragment

interface SomeInterface {
    fun showDetailInfo(bookItem: BookItem)
    fun showResultInfo(query: String)
}

class MainActivity : AppCompatActivity(), SomeInterface {
    companion object {
        private const val TAG = "BookDBMainAct"
    }

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((application as BooksApplication).repository)
    }

    private val bookListFragment = BookListFragment()
    private val groupListFragment = GroupListFragment()
    private val searchFragment = SearchFragment()
    private val cameraFragment = CameraFragment()
    private val detailFragment = DetailFragment()
    private val resultFragment = ResultFragment()

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
            supportFragmentManager.beginTransaction()
                .add(viewBinding.frameLayoutFull.id, groupListFragment)
                .addToBackStack(null)
                .commit()
        }

        viewBinding.btnOpenCamera.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(viewBinding.frameLayoutFull.id, cameraFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun showDetailInfo(bookItem: BookItem) {
        detailFragment.setBookItem(bookItem)

        supportFragmentManager.beginTransaction()
            .add(viewBinding.frameLayoutFull.id, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun showResultInfo(query: String) {
        resultFragment.setQuery(query)

        supportFragmentManager.beginTransaction()
            .add(viewBinding.frameLayoutFull.id, resultFragment)
            .addToBackStack(null)
            .commit()
    }
}