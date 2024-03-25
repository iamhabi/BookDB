package com.habidev.bookdb.ui.book

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.fragment.app.DialogFragment
import com.habidev.bookdb.R
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.databinding.BookListMoreBinding

class BookMoreDialogFragment : DialogFragment(R.layout.book_list_more) {
    private lateinit var viewBinding: BookListMoreBinding

    private var bookItem: BookItem? = null

    private var onMoreListener: BookMoreBottomSheetFragment.OnMoreListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = BookListMoreBinding.bind(view)

        dialog?.window?.setLayout(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        initViewListener()
    }

    override fun onResume() {
        super.onResume()

        updateInfo(bookItem)
    }

    fun setBookItem(bookItem: BookItem) {
        this.bookItem = bookItem
    }

    fun setListener(listener: BookMoreBottomSheetFragment.OnMoreListener) {
        this.onMoreListener = listener
    }

    private fun updateInfo(bookItem: BookItem?) {
        val item = bookItem ?: return

        viewBinding.textViewTitle.text = item.title
    }

    private fun initViewListener() {
        viewBinding.btnOpenInBrowser.setOnClickListener {
            val bookItem = this.bookItem ?: return@setOnClickListener
            val link: Uri = Uri.parse(bookItem.link)
            val intent = Intent(Intent.ACTION_VIEW, link)
            startActivity(intent)
        }

        viewBinding.btnDelete.setOnClickListener {
            this.bookItem?.let {
                onMoreListener?.onRemove(it)
            }

            dismiss()
        }

        viewBinding.btnAddToGroup.setOnClickListener {
            this.bookItem?.let {
                onMoreListener?.onAddToGroup(it)
            }

            dismiss()
        }
    }
}