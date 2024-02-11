package com.habidev.bookdb.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.habidev.bookdb.R
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.database.BookViewModel
import com.habidev.bookdb.databinding.DetailBinding
import com.habidev.bookdb.utils.Utils

class DetailFragment: Fragment() {
    companion object {
        private const val TAG = "BookDBDetailFrag"
    }

    private val bookViewModel: BookViewModel by activityViewModels()

    private lateinit var viewBinding: DetailBinding

    private lateinit var bookItem: BookItem

    private var preventCommentListener: Boolean = false
    private var preventReadListener: Boolean = false
    private var preventOwnListener: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = DetailBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewListener()

        initReadStateSpinner()
        initOwnStateSpinner()
    }

    override fun onStart() {
        super.onStart()

        updateInfo()
    }

    fun setBookItem(bookItem: BookItem) {
        this.bookItem = bookItem
    }

    private fun updateInfo() {
        if (!this::bookItem.isInitialized) {
            return
        }

        preventCommentListener = true
        preventReadListener = true
        preventOwnListener = true

        Glide
            .with(this)
            .load(Uri.parse(bookItem.imageUrl))
            .placeholder(R.drawable.book)
            .error(R.drawable.book)
            .into(viewBinding.imageViewBookCover)

        viewBinding.textViewTitle.text = bookItem.title
        viewBinding.textViewAuthor.text = bookItem.author
        viewBinding.textViewDescription.text = bookItem.description

        bookItem.comment?.let { comment ->
            viewBinding.editTextComment.setText(comment, TextView.BufferType.EDITABLE)
        }

        viewBinding.spinnerReadingState.setSelection(bookItem.readingState)
        viewBinding.spinnerOwnState.setSelection(bookItem.ownState)
    }

    private fun initViewListener() {
        Utils.setUpEditTextCloseKeyboard(requireActivity(), viewBinding.root)

        viewBinding.btnOpenInBrowser.setOnClickListener {
            if (this::bookItem.isInitialized) {
                val link: Uri = Uri.parse(bookItem.link)
                val intent = Intent(Intent.ACTION_VIEW, link)
                startActivity(intent)
            }
        }

        viewBinding.btnClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewBinding.editTextComment.addTextChangedListener { text ->
            if (!preventCommentListener && this@DetailFragment::bookItem.isInitialized) {
                text?.let {
                    bookItem.comment = it.toString()

                    bookViewModel.updateBook(bookItem)
                }

                preventCommentListener = false
            }
        }

        viewBinding.editTextComment.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Utils.showKeyBoard(requireActivity(), view)
            } else {
                Utils.closeKeyBoard(requireActivity())
            }
        }

        viewBinding.spinnerReadingState.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!preventReadListener && this@DetailFragment::bookItem.isInitialized) {
                    bookItem.readingState = position

                    bookViewModel.updateBook(bookItem)

                    preventReadListener = false
                }
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
                if (!preventOwnListener && this@DetailFragment::bookItem.isInitialized) {
                    bookItem.ownState = position

                    bookViewModel.updateBook(bookItem)

                    preventOwnListener = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun initReadStateSpinner() {
        val readStateAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            BookItem.READ_STATE
        )

        readStateAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)

        viewBinding.spinnerReadingState.adapter = readStateAdapter
    }

    private fun initOwnStateSpinner() {
        val ownStateAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            BookItem.OWN_STATE
        )

        ownStateAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)

        viewBinding.spinnerOwnState.adapter = ownStateAdapter
    }
}