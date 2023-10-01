package com.habidev.bookdb.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.activity.DetailActivity
import com.habidev.bookdb.adapter.BookListAdapter
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.database.BookViewModel
import com.habidev.bookdb.databinding.BookListBinding

class BookListFragment: Fragment() {
    private val bookViewModel: BookViewModel by activityViewModels()

    private lateinit var viewBinding: BookListBinding

    private lateinit var adapter: BookListAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager

    private lateinit var bookMoreFragment: BookMoreFragment

    private val onItemClickListener = object: BookListAdapter.OnItemClickListener {
        override fun onClick(position: Int, bookItem: BookItem) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("bookItem", bookItem)
            startActivity(intent)
        }

        override fun onMoreClick(position: Int, bookItem: BookItem) {
            bookMoreFragment.setBookItem(bookItem)
            bookMoreFragment.show(childFragmentManager, null)
        }
    }

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

        bookMoreFragment = BookMoreFragment()

        initRecyclerView()
        initViewListener()
    }

    override fun onStart() {
        super.onStart()

        bookViewModel.allBooks.observe(this) { books ->
            adapter.checkItemExist(books)
            adapter.add(books)
        }
    }

    override fun onStop() {
        super.onStop()

        bookViewModel.allBooks.removeObservers(this)
    }

    private fun initRecyclerView() {
        adapter = BookListAdapter(requireContext())

        adapter.setOnItemClickListener(onItemClickListener)

        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        gridLayoutManager = GridLayoutManager(context, 2)

        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.layoutManager = linearLayoutManager
    }

    private fun initViewListener() {
        viewBinding.btnToggleListLayout.setOnCheckedChangeListener { _, isChecked ->
            viewBinding.recyclerView.layoutManager = if (isChecked) {
                gridLayoutManager
            } else {
                linearLayoutManager
            }

            adapter.changeLayout(isChecked)

            viewBinding.recyclerView.adapter = adapter
        }
    }
}