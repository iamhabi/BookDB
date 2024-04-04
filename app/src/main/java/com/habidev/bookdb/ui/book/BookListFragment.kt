package com.habidev.bookdb.ui.book

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.R
import com.habidev.bookdb.adapter.BookListAdapter
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.data.GroupBookItem
import com.habidev.bookdb.data.GroupItem
import com.habidev.bookdb.databinding.BookListBinding
import com.habidev.bookdb.ui.main.SomeInterface
import com.habidev.bookdb.viewmodel.BookDBViewModel
import com.habidev.bookdb.viewmodel.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BookListFragment: Fragment(R.layout.book_list) {
    private val bookDBViewModel: BookDBViewModel by activityViewModels()
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    private lateinit var viewBinding: BookListBinding

    private lateinit var adapter: BookListAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager

    private val groupSelectFragment = GroupSelectFragment()

    private val bookMoreDialogFragment = BookMoreDialogFragment()
    private val bookMoreBottomSheetFragment = BookMoreBottomSheetFragment()

    private var someInterface: SomeInterface? = null

    private var booksByGroupLiveData: LiveData<List<BookItem>>? = null

    private var groupItem: GroupItem? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        someInterface = context as? SomeInterface
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = BookListBinding.bind(view)

        initRecyclerView()
        initBookMoreFrag()
    }

    override fun onStart() {
        super.onStart()

        updateAllBooks()

        observeSettings()
    }

    override fun onStop() {
        super.onStop()

        removeObservers()
    }

    fun updateAllBooks() {
        removeObservers()

        adapter.clear()

//        bookDBViewModel.allBooksLiveData.observe(requireActivity()) { books ->
//            adapter.add(books)
//        }

        bookDBViewModel.allBooks.observe(requireActivity()) { books ->
            adapter.add(books)
            updateUIBySettings()
        }

        groupItem = null
    }

    fun updateBooksByGroup(groupItem: GroupItem) {
        this.groupItem = groupItem

        removeObservers()

        adapter.clear()

        booksByGroupLiveData = bookDBViewModel.booksByGroupLiveData(groupItem)

        booksByGroupLiveData?.observe(requireActivity()) { books ->
            adapter.add(books)
        }
    }

    private fun updateUIBySettings() {
        val settings = settingsViewModel.settings.value ?: return

        val isSortByTitle = settings.isSortByTitle == 1
        val isGrid = settings.isGird == 1

        if (isSortByTitle) {
            adapter.sortByTitle()
        } else {
            adapter.sortByAuthor()
        }

        adapter.changeLayout(isGrid)

        viewBinding.recyclerView.run {
            layoutManager = if (isGrid) {
                gridLayoutManager
            } else {
                linearLayoutManager
            }

            this.adapter = adapter
        }
    }

    private fun observeSettings() {
        settingsViewModel.settings.observe(requireActivity()) { settings ->
            CoroutineScope(Dispatchers.Main).launch {
                if (settings.isSortByTitle == 1) {
                    adapter.sortByTitle()
                } else {
                    adapter.sortByAuthor()
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                val isGrid = settings.isGird == 1

                adapter.changeLayout(isGrid)

                viewBinding.recyclerView.run {
                    layoutManager = if (isGrid) {
                        gridLayoutManager
                    } else {
                        linearLayoutManager
                    }

                    this.adapter = adapter
                }
            }
        }
    }

    private fun removeObservers() {
        bookDBViewModel.allBooksLiveData.removeObservers(requireActivity())
        booksByGroupLiveData?.removeObservers(requireActivity())
        settingsViewModel.settings.removeObservers(requireActivity())
    }

    private fun initRecyclerView() {
        adapter = BookListAdapter(requireContext())

        adapter.setOnItemClickListener(OnItemClickListener())

        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val isTablet: Boolean = resources.getBoolean(R.bool.isTablet)

        val spanCount = if (isTablet) 3 else 2

        gridLayoutManager = GridLayoutManager(context, spanCount)

        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.layoutManager = linearLayoutManager
    }

    private fun initBookMoreFrag() {
        bookMoreDialogFragment.setListener(BookMoreListener())
        bookMoreBottomSheetFragment.setListener(BookMoreListener())
    }

    private fun showMore(bookItem: BookItem) {
        val isTablet: Boolean = resources.getBoolean(R.bool.isTablet)

        if (isTablet) {
            bookMoreDialogFragment.run {
                setBookItem(bookItem)
                show(this@BookListFragment.childFragmentManager, null)
            }
        } else {
            bookMoreBottomSheetFragment.run {
                setBookItem(bookItem)
                show(this@BookListFragment.childFragmentManager, null)
            }
        }
    }

    inner class BookMoreListener : BookMoreBottomSheetFragment.OnMoreListener {
        override fun onRemove(bookItem: BookItem) {
            adapter.remove(bookItem)

            if (groupItem == null) {
                bookDBViewModel.deleteBook(bookItem)
            } else {
                bookDBViewModel.deleteBookFromGroup(GroupBookItem(0, groupItem!!.id, bookItem.isbn))
            }
        }

        override fun onAddToGroup(bookItem: BookItem) {
            groupSelectFragment.setBookItem(bookItem)
            groupSelectFragment.show(childFragmentManager, null)
        }
    }

    inner class OnItemClickListener : BookListAdapter.OnItemClickListener {
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
}