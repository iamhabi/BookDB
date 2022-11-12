package com.habidev.bookdb

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.habidev.bookdb.database.BookDao
import com.habidev.bookdb.database.BookDatabase
import com.habidev.bookdb.databinding.BookListBinding

class BookListFragment: Fragment() {
    private lateinit var viewBinding: BookListBinding
    private lateinit var bookViewModel: BookViewModel

    private lateinit var bookDao: BookDao

    private lateinit var items: List<BookItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = BookListBinding.inflate(inflater, container, false)
        bookViewModel = BookViewModel()

//        initBookDao()

//        items = bookDao.getAll()

        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()

//        items = bookViewModel.bookItemList

//        val adapter = BookAdapter(requireContext(), items)

//        viewBinding.bookRecyclerView.adapter = adapter
    }

    private fun initBookDao() {
        val database = Room.databaseBuilder(
            requireContext(),
            BookDatabase::class.java, "books"
        ).build()

        bookDao = database.bookDao()
    }
}