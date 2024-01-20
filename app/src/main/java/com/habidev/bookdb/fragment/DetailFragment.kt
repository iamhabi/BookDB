package com.habidev.bookdb.fragment

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
import com.habidev.bookdb.database.BookGroupItem
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
    private lateinit var groupList: List<BookGroupItem>

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

        initGroupSpinner()
        initReadStateSpinner()
        initOwnStateSpinner()
    }

    override fun onStop() {
        super.onStop()

        bookViewModel.allGroupsLiveData.removeObservers(requireActivity())
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
        viewBinding.textViewDescription.text = bookItem.description
        viewBinding.editTextComment.setText(bookItem.comment.toString(), TextView.BufferType.EDITABLE)

        viewBinding.spinnerReadingState.setSelection(bookItem.readingState)
        viewBinding.spinnerOwnState.setSelection(bookItem.ownState)

        // TODO
        //  set group

        if (this::groupList.isInitialized) {
            groupList.indexOfFirst {
                it.title == bookItem.group
            }.let { index ->
                viewBinding.spinnerGroup.setSelection(index)
            }
        }
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

        viewBinding.editTextComment.addTextChangedListener { text ->
            if (this@DetailFragment::bookItem.isInitialized) {
                text?.let {
                    bookItem.comment = it.toString()

                    bookViewModel.updateBook(bookItem)
                }
            }
        }

        viewBinding.editTextComment.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Utils.showKeyboard(requireContext(), view)
            } else {
                Utils.hideKeyboard(requireContext(), view)
            }
        }

        viewBinding.spinnerGroup.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    // TODO
                    //  Dialog to create new group
                } else {
                    if (this@DetailFragment::bookItem.isInitialized) {
                        val group = groupList[position]

                        bookItem.group = group.title

                        bookViewModel.updateBook(bookItem)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        viewBinding.spinnerReadingState.onItemSelectedListener = object : OnItemSelectedListener {
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

        viewBinding.spinnerOwnState.onItemSelectedListener = object : OnItemSelectedListener {
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
    }

    private fun initGroupSpinner() {
        bookViewModel.allGroupsLiveData.observe(requireActivity()) { groupList ->
            this.groupList = groupList

            val groupArray = arrayListOf<String>()

            for (group in groupList) {
                groupArray.add(group.title)
            }

            groupArray.add("Create new group")

            val groupAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                groupArray
            )

            groupAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)

            viewBinding.spinnerGroup.adapter = groupAdapter
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