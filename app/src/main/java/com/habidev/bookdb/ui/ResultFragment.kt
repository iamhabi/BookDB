package com.habidev.bookdb.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.habidev.bookdb.R
import com.habidev.bookdb.api.BookDBClient
import com.habidev.bookdb.api.search.SearchClient
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.databinding.ResultBinding
import com.habidev.bookdb.viewmodel.BookDBViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultFragment: Fragment(R.layout.result) {
    private val bookDBViewModel: BookDBViewModel by activityViewModels()

    private lateinit var viewBinding: ResultBinding

    private var bookItem: BookItem? = null

    private lateinit var query: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DataBindingUtil.bind<ResultBinding>(view)?.let { binding ->
            viewBinding = binding
        }

        savedInstanceState?.let { bundle ->
            query = bundle.getString("query", "")
        }

        if (this::query.isInitialized) {
            SearchClient.searchDetail(
                query
            ) { result ->
                bookItem = result

                CoroutineScope(Dispatchers.Main).launch {
                    viewBinding.item = result
                }
            }
        }

        initViewListener()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("query", query)
    }

    fun setQuery(query: String) {
        this.query = query
    }

    private fun initViewListener() {
        viewBinding.btnOpenInBrowser.setOnClickListener {
            val linkUri = Uri.parse(bookItem?.link)
            val intent = Intent(Intent.ACTION_VIEW, linkUri)
            startActivity(intent)
        }

        viewBinding.btnAddBookmark.setOnClickListener {
            val bookItem = bookItem ?: return@setOnClickListener

            BookDBClient.addBook(bookItem)

            CoroutineScope(Dispatchers.IO).launch {
                val isInsertSuccess = bookDBViewModel.insertBook(bookItem)

                val message = if (isInsertSuccess) {
                    R.string.insert_book_success_message
                } else {
                    R.string.insert_book_failure_message
                }

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewBinding.btnClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}