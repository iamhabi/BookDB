package com.habidev.bookdb.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.habidev.bookdb.R
import com.habidev.bookdb.api.SearchClient
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.databinding.ResultBinding
import com.habidev.bookdb.viewmodel.BookDBViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class ResultFragment: Fragment() {
    private val bookDBViewModel: BookDBViewModel by activityViewModels()

    private lateinit var viewBinding: ResultBinding

    private lateinit var resultJsonObject: JSONObject

    private lateinit var bookItem: BookItem

    private lateinit var query: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = ResultBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewListener()

        savedInstanceState?.let { bundle ->
            query = bundle.getString("query", "")
        }

        if (this::query.isInitialized) {
            SearchClient.search(
                query,
                true
            ) { result ->
                parseBookInfo(result)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("query", query)
    }

    fun setQuery(query: String) {
        this.query = query
    }

    private fun parseBookInfo(result: String) {
        resultJsonObject = JSONObject(result).getJSONArray("items").getJSONObject(0)

        val isbn = (resultJsonObject.get("isbn") as String).toLong()
        val link = resultJsonObject.get("link") as String
        val title = resultJsonObject.get("title") as String
        val author = resultJsonObject.get("author") as String
        val imageUrl = resultJsonObject.get("image") as String
        val description = resultJsonObject.get("description") as String

        bookItem = BookItem(
            isbn,
            link,
            title,
            author,
            imageUrl,
            description
        )

        CoroutineScope(Dispatchers.Main).launch {
            updateBookInfo()
        }
    }

    private fun updateBookInfo() {
        Glide
            .with(this)
            .load(Uri.parse(bookItem.imageUrl))
            .placeholder(R.drawable.book)
            .error(R.drawable.book)
            .into(viewBinding.imageViewBookCover)

        viewBinding.textViewTitle.text = bookItem.title
        viewBinding.textViewAuthor.text = bookItem.author
        viewBinding.textViewDescription.text = bookItem.description
    }

    private fun initViewListener() {
        viewBinding.btnOpenInBrowser.setOnClickListener {
            if (this::bookItem.isInitialized) {
                val linkUri = Uri.parse(bookItem.link)
                val intent = Intent(Intent.ACTION_VIEW, linkUri)
                startActivity(intent)
            }
        }

        viewBinding.btnAddBookmark.setOnClickListener {
            if (this::bookItem.isInitialized) {
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
        }

        viewBinding.btnClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}