package com.habidev.bookdb

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
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

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookAdapter(requireContext(), items, onItemClickListener)

        viewBinding.bookRecyclerView.adapter = adapter
        viewBinding.bookRecyclerView.layoutManager = LinearLayoutManager(context)

        getBooksFromDao()
    }

    private val onItemClickListener = object: BookAdapter.OnItemClickListener {
        override fun onClick(position: Int) {
            val bookItem = items[position]

            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("bookItem", bookItem)
            startActivity(intent)
        }

        override fun onLongClick(position: Int): Boolean {
            return true
        }
    }

    private fun initBookDao() {
        val database = Room.databaseBuilder(
            requireContext(),
            BookDatabase::class.java, "books"
        ).build()

        bookDao = database.bookDao()
    }

    private fun getBooksFromDao() {
        runBlocking {
            items = bookDao.getAllBooks()
        }

        adapter.notifyItemRangeChanged(0, items.size)
    }
}