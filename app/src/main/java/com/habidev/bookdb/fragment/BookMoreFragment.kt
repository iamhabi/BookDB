package com.habidev.bookdb.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.database.BookViewModel
import com.habidev.bookdb.databinding.BookListMoreBinding

class BookMoreFragment : BottomSheetDialogFragment() {
    companion object {
        private const val TAG = "BookMore"
    }

    private val bookViewModel: BookViewModel by activityViewModels()

    private lateinit var viewBinding: BookListMoreBinding

    private var bookItem: BookItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = BookListMoreBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewListener()
    }

    override fun onResume() {
        super.onResume()

        val bookItem = this.bookItem ?: return

        viewBinding.textViewTitle.text = bookItem.title
    }

    fun setBookItem(bookItem: BookItem) {
        this.bookItem = bookItem
    }

    private fun initViewListener() {
        viewBinding.textViewDelete.setOnClickListener {
            Log.d(TAG, "Delete")
        }

        viewBinding.layoutReadingState.setOnClickListener {
            Log.d(TAG, "Reading state")
        }

        viewBinding.layoutOwnState.setOnClickListener {
            Log.d(TAG, "Own state")
        }
    }
}