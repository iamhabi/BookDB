package com.habidev.bookdb

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.habidev.bookdb.BookAdapter.onItemClickListener
import com.habidev.bookdb.database.BookDao
import com.habidev.bookdb.database.BookDatabase
import com.habidev.bookdb.databinding.BookListBinding
import kotlinx.coroutines.runBlocking

class BookListFragment: Fragment() {
    private lateinit var viewBinding: BookListBinding
    private lateinit var bookViewModel: BookViewModel

    private lateinit var bookDao: BookDao

    private lateinit var items: List<BookItem>
    private lateinit var adapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = BookListBinding.inflate(inflater, container, false)
        bookViewModel = BookViewModel()

        initBookDao()

        runBlocking {
            items = bookDao.getAll()
        }

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookAdapter(requireContext(), items)

        viewBinding.bookRecyclerView.adapter = adapter
        viewBinding.bookRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter.notifyItemRangeChanged(0, items.size)
    }

    val onItemClickListener = object: onItemClickListener {
        override fun onClick(position: Int) {

        }

        override fun onLongClick(position: Int) {
            TODO("Not yet implemented")
        }

    }

    private fun initBookDao() {
        val database = Room.databaseBuilder(
            requireContext(),
            BookDatabase::class.java, "books"
        ).build()

        bookDao = database.bookDao()
    }
}