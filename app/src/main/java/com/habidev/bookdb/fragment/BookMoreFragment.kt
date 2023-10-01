package com.habidev.bookdb.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
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

        updateInfo(bookItem)
    }

    fun setBookItem(bookItem: BookItem) {
        this.bookItem = bookItem
    }

    private fun updateInfo(bookItem: BookItem?) {
        val item = bookItem ?: return

        viewBinding.textViewTitle.text = item.title

        viewBinding.spinnerReadingState.setSelection(item.readingState)
        viewBinding.spinnerOwnState.setSelection(item.ownState)
    }

    private fun initViewListener() {
        viewBinding.textViewDelete.setOnClickListener {
            val bookItem = this.bookItem ?: return@setOnClickListener

            bookViewModel.delete(bookItem)

            dismiss()
        }

        val readStateAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            BookItem.READ_STATE
        )

        val ownStateAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            BookItem.OWN_STATE
        )

        readStateAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
        ownStateAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)

        viewBinding.spinnerReadingState.adapter = readStateAdapter
        viewBinding.spinnerOwnState.adapter = ownStateAdapter

        viewBinding.spinnerReadingState.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val bookItem = this@BookMoreFragment.bookItem ?: return

                bookItem.readingState = position

                bookViewModel.update(bookItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        viewBinding.spinnerOwnState.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val bookItem = this@BookMoreFragment.bookItem ?: return

                bookItem.ownState = position

                bookViewModel.update(bookItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}