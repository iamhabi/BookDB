package com.habidev.bookdb.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.R
import com.habidev.bookdb.adapter.BookListAdapter
import com.habidev.bookdb.adapter.SearchAdapter
import com.habidev.bookdb.api.SearchClient
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.databinding.SearchBinding
import com.habidev.bookdb.ui.book.BookMoreBottomSheetFragment
import com.habidev.bookdb.ui.main.SomeInterface
import com.habidev.bookdb.utils.Utils
import com.habidev.bookdb.viewmodel.BookDBViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class SearchFragment : Fragment() {
    private lateinit var viewBinding: SearchBinding

    private var dbAdapter: SearchAdapter? = null
    private var internetAdapter: SearchAdapter? = null

    private var someInterface: SomeInterface? = null

    private val bookDBViewModel: BookDBViewModel by activityViewModels()

    private val bookMoreBottomSheetFragment: BookMoreBottomSheetFragment = BookMoreBottomSheetFragment()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        someInterface = context as? SomeInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = SearchBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initViewListener()
    }

    override fun onResume() {
        super.onResume()

        if (viewBinding.editTextSearch.text.toString() == "") {
            viewBinding.editTextSearch.requestFocus()

            Utils.showKeyboard(requireActivity(), viewBinding.editTextSearch)
        }
    }

    override fun onStop() {
        super.onStop()

        viewBinding.editTextSearch.setText("")
    }

    private fun showResultInfo(bookItem: BookItem) {
        someInterface?.showResultInfo(bookItem.isbn.toString())

        Utils.closeKeyboard(requireActivity())
    }

    private fun showMore(bookItem: BookItem) {
        bookMoreBottomSheetFragment.run {
            setBookItem(bookItem)
            show(this@SearchFragment.childFragmentManager, null)
        }
    }

    private fun searchDatabase(query: String) {
        if (query == "") {
            dbAdapter?.clear()

            return
        }

        dbAdapter?.deleteNotMatchedItems(query)

        CoroutineScope(Dispatchers.IO).launch {
            val resultList = bookDBViewModel.searchBook(query)

            CoroutineScope(Dispatchers.Main).launch {
                dbAdapter?.add(resultList)
            }
        }
    }

    private fun searchInternet(query: String) {
        if (query == "") {
            internetAdapter?.clear()

            return
        }

        internetAdapter?.deleteNotMatchedItems(query)

        SearchClient.search(query) { result ->
            parseResult(result)
        }
    }

    private fun parseResult(resultJson: String) {
        val resultJsonArray = JSONObject(resultJson).getJSONArray("items")

        for (i in 0 until resultJsonArray.length()) {
            val jsonObject = resultJsonArray.getJSONObject(i)

            val isbn = (jsonObject.get("isbn") as String).toLong()
            val link = jsonObject.get("link") as String
            val title = jsonObject.get("title") as String
            val author = jsonObject.get("author") as String
            val imageUrl = jsonObject.get("image") as String
            val description = jsonObject.get("description") as String

            val bookItem = BookItem(
                isbn,
                link,
                title,
                author,
                imageUrl,
                description
            )

            CoroutineScope(Dispatchers.Main).launch {
                internetAdapter?.add(bookItem)
            }
        }
    }

    private fun initRecyclerView() {
        dbAdapter = SearchAdapter(
            requireContext(),
            R.string.database
        ).apply {
            setOnItemClickListener(object : BookListAdapter.OnItemClickListener {
                override fun onClick(position: Int, bookItem: BookItem) {
                    showResultInfo(bookItem)
                }

                override fun onLongClick(position: Int, bookItem: BookItem) {
                    showResultInfo(bookItem)
                }

                override fun onMoreClick(position: Int, bookItem: BookItem) {
                    showMore(bookItem)
                }
            })
        }

        internetAdapter = SearchAdapter(
            requireContext(),
            R.string.internet
        ).apply {
            setOnItemClickListener(object : BookListAdapter.OnItemClickListener {
                override fun onClick(position: Int, bookItem: BookItem) {
                    showResultInfo(bookItem)
                }

                override fun onLongClick(position: Int, bookItem: BookItem) {
                    showResultInfo(bookItem)
                }

                override fun onMoreClick(position: Int, bookItem: BookItem) {
                }
            })
        }

        val concatAdapter = ConcatAdapter(dbAdapter, internetAdapter)
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        viewBinding.recyclerViewSearchResult.adapter = concatAdapter
        viewBinding.recyclerViewSearchResult.layoutManager = layoutManager
    }

    private fun initViewListener() {
        Utils.setUpEditTextCloseKeyboard(requireActivity(), viewBinding.root)

        viewBinding.btnClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewBinding.editTextSearch.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                view.text.toString().let { query ->
                    searchInternet(query)
                }

                Utils.closeKeyboard(requireActivity())
            }

            false
        }

        viewBinding.editTextSearch.addTextChangedListener { text ->
            text?.toString()?.let { query ->
                searchDatabase(query)
            }
        }

        viewBinding.editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Utils.showKeyboard(requireActivity(), view)
            } else {
                Utils.closeKeyboard(requireActivity())
            }
        }
    }
}