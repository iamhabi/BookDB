package com.habidev.bookdb.ui.book

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.adapter.BookListAdapter
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.data.GroupItem
import com.habidev.bookdb.databinding.BookListBinding
import com.habidev.bookdb.ui.main.SomeInterface
import com.habidev.bookdb.viewmodel.BookDBViewModel

class BookListFragment: Fragment() {
    private val bookDBViewModel: BookDBViewModel by activityViewModels()

    private lateinit var viewBinding: BookListBinding

    private lateinit var adapter: BookListAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager

    private lateinit var bookBottomSheetFragment: BookBottomSheetFragment
    private lateinit var groupSelectFragment: GroupSelectFragment

    private val onItemClickListener = object: BookListAdapter.OnItemClickListener {
        override fun onClick(position: Int, bookItem: BookItem) {
            someInterface?.showDetailInfo(bookItem)
        }

        override fun onLongClick(position: Int, bookItem: BookItem) {
            showMore(bookItem)
        }

        override fun onMoreClick(position: Int, bookItem: BookItem) {
            showMore(bookItem)
        }
    }

    private var someInterface: SomeInterface? = null

    private var booksByGroupLiveData: LiveData<List<BookItem>>? = null

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

        initRecyclerView()
        initViewListener()
        initBookMoreFrag()

        groupSelectFragment = GroupSelectFragment()
    }

    override fun onStart() {
        super.onStart()

        updateAllBooks()
    }

    override fun onStop() {
        super.onStop()

        removeObservers()
    }

    fun updateAllBooks() {
        removeObservers()

        adapter.clear()

        bookDBViewModel.allBooksLiveData.observe(requireActivity()) { books ->
            adapter.add(books)
        }
    }

    fun updateBooksByGroup(groupItem: GroupItem) {
        removeObservers()

        adapter.clear()

        booksByGroupLiveData = bookDBViewModel.booksByGroupLiveData(groupItem)

        booksByGroupLiveData?.observe(requireActivity()) { books ->
            adapter.add(books)
        }
    }

    private fun removeObservers() {
        bookDBViewModel.allBooksLiveData.removeObservers(requireActivity())
        booksByGroupLiveData?.removeObservers(requireActivity())
    }

    private fun initRecyclerView() {
        adapter = BookListAdapter(requireContext())

        adapter.setOnItemClickListener(onItemClickListener)

        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        gridLayoutManager = GridLayoutManager(context, 2)

        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.layoutManager = linearLayoutManager
    }

    private fun initBookMoreFrag() {
        bookBottomSheetFragment = BookBottomSheetFragment()

        bookBottomSheetFragment.setListener(object : BookBottomSheetFragment.OnMoreListener {
            override fun onRemove(bookItem: BookItem) {
                adapter.remove(bookItem)

                bookDBViewModel.deleteBook(bookItem)
            }

            override fun onAddToGroup(bookItem: BookItem) {
                groupSelectFragment.setBookItem(bookItem)
                groupSelectFragment.show(childFragmentManager, null)
            }
        })
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

    private fun showMore(bookItem: BookItem) {
        bookBottomSheetFragment.run {
            setBookItem(bookItem)
            show(this@BookListFragment.childFragmentManager, null)
        }
    }
}