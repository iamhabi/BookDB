package com.habidev.bookdb.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.habidev.bookdb.R
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.databinding.DetailBinding
import com.habidev.bookdb.utils.Utils
import com.habidev.bookdb.viwemodel.BookDBViewModel

class DetailFragment: Fragment() {
    private val bookDBViewModel: BookDBViewModel by activityViewModels()

    private lateinit var viewBinding: DetailBinding

    private lateinit var bookItem: BookItem

    private var preventCommentListener: Boolean = false

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

                    bookDBViewModel.updateBook(bookItem)
                }

                preventCommentListener = false
            }
        }

        viewBinding.editTextComment.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Utils.showKeyboard(requireActivity(), view)
            } else {
                Utils.closeKeyboard(requireActivity())
            }
        }
    }
}