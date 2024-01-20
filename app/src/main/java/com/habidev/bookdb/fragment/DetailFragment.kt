package com.habidev.bookdb.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
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

        updateInfo()
        initViewListener()
    }

    fun setBookItem(bookItem: BookItem) {
        this.bookItem = bookItem
    }

    private fun updateInfo() {
        if (!this::bookItem.isInitialized) {
            return
        }

        Glide
            .with(this)
            .load(Uri.parse(bookItem.imageUrl))
            .placeholder(R.drawable.book)
            .error(R.drawable.book)
            .into(viewBinding.imageViewBookCover)

        viewBinding.textViewTitle.text = bookItem.title
        viewBinding.textViewAuthor.text = bookItem.author
        viewBinding.textViewGroup.text = bookItem.group
        viewBinding.textViewDescription.text = bookItem.description
        viewBinding.editTextComment.setText(bookItem.comment.toString(), TextView.BufferType.EDITABLE)

        viewBinding.spinnerReadingState.setSelection(bookItem.readingState)
        viewBinding.spinnerOwnState.setSelection(bookItem.ownState)
    }

    private fun initViewListener() {
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

        viewBinding.spinnerReadingState.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (this@DetailFragment::bookItem.isInitialized) {
                    bookItem.readingState = position

                    bookViewModel.updateBook(bookItem)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        viewBinding.spinnerOwnState.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (this@DetailFragment::bookItem.isInitialized) {
                    bookItem.ownState = position

                    bookViewModel.updateBook(bookItem)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        viewBinding.editTextComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(comment: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (this@DetailFragment::bookItem.isInitialized) {
                    bookItem.comment = comment.toString()

                    bookViewModel.updateBook(bookItem)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        viewBinding.editTextComment.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Utils.showKeyboard(requireContext(), view)
            } else {
                Utils.hideKeyboard(requireContext(), view)
            }
        }
    }
}