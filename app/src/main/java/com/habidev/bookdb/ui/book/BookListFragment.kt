package com.habidev.bookdb.ui.book

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.adapter.BookListAdapter
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.database.BookViewModel
import com.habidev.bookdb.databinding.BookListBinding
import com.habidev.bookdb.ui.main.SomeInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookListFragment: Fragment() {
    companion object {
        private const val TAG = "BookDBList"
    }

    private val bookViewModel: BookViewModel by activityViewModels()

    private lateinit var viewBinding: BookListBinding

    private lateinit var adapter: BookListAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager

    private lateinit var bookMoreFragment: BookMoreFragment

    private val onItemClickListener = object: BookListAdapter.OnItemClickListener {
        override fun onClick(position: Int, bookItem: BookItem) {
            someInterface?.showDetailInfo(bookItem)
        }

        override fun onLongClick(position: Int, bookItem: BookItem) {
            bookMoreFragment.setBookItem(bookItem)
            bookMoreFragment.show(childFragmentManager, null)
        }

        override fun onMoreClick(position: Int, bookItem: BookItem) {
            bookMoreFragment.setBookItem(bookItem)
            bookMoreFragment.show(childFragmentManager, null)
        }
    }

    private var someInterface: SomeInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        someInterface = context as? SomeInterface
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

        bookViewModel.allBooksLiveData.observe(requireActivity()) { books ->
            adapter.add(books)
        }

        bookViewModel.groupLiveData.observe(requireActivity()) { groupItem ->
            groupItem?.let {
                adapter.clear()

                if (it.title == "All") {
                    bookViewModel.allBooksLiveData.value?.let { books ->
                        adapter.add(books)
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val groupBookList = bookViewModel.getBooksByGroup(it)

                        CoroutineScope(Dispatchers.Main).launch {
                            adapter.add(groupBookList)
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()

        bookViewModel.allBooksLiveData.removeObservers(requireActivity())
        bookViewModel.groupLiveData.removeObservers(requireActivity())
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