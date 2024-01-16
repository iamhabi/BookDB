package com.habidev.bookdb.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
    private val cameraFragment = CameraFragment()

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

        viewBinding.btnOpenCamera.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(viewBinding.frameLayoutFull.id, cameraFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}