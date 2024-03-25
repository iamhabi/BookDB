package com.habidev.bookdb.ui.book

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.habidev.bookdb.R
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.databinding.BookListMoreBinding

class BookMoreBottomSheetFragment : BottomSheetDialogFragment(R.layout.book_list_more) {
    interface OnMoreListener {
        fun onRemove(bookItem: BookItem)
        fun onAddToGroup(bookItem: BookItem)
    }

    private lateinit var viewBinding: BookListMoreBinding

    private var bookItem: BookItem? = null

    private var onMoreListener: OnMoreListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DataBindingUtil.bind<BookListMoreBinding>(view)?.let { binding ->
            viewBinding = binding

            viewBinding.item = bookItem
        }

        initViewListener()
    }

    fun setBookItem(bookItem: BookItem) {
        this.bookItem = bookItem
    }

    fun setListener(listener: OnMoreListener) {
        this.onMoreListener = listener
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