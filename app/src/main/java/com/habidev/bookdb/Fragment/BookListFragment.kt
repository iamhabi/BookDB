package com.habidev.bookdb.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.habidev.bookdb.Activity.DetailActivity
import com.habidev.bookdb.BookItem
import com.habidev.bookdb.Adapter.BookListAdapter
import com.habidev.bookdb.BookViewModel
import com.habidev.bookdb.databinding.BookListBinding

class BookListFragment: Fragment() {
    private lateinit var viewBinding: BookListBinding

    private val items: MutableList<BookItem> = mutableListOf()
    private lateinit var adapter: BookListAdapter

    private val bookViewModel: BookViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = BookListBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookListAdapter(requireContext(), items, onItemClickListener)

        viewBinding.bookRecyclerView.adapter = adapter
        viewBinding.bookRecyclerView.layoutManager = LinearLayoutManager(context)

        bookViewModel.allBooks.observe(requireActivity()) { books ->
            books.let {
                items.clear()
                items.addAll(books)

                adapter.notifyItemRangeChanged(0, items.size)
            }
        }
    }

    private val onItemClickListener = object: BookListAdapter.OnItemClickListener {
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
}