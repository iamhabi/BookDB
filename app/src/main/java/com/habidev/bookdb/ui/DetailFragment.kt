package com.habidev.bookdb.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.habidev.bookdb.R
import com.habidev.bookdb.databinding.DetailBinding
import com.habidev.bookdb.utils.Utils
import com.habidev.bookdb.viewmodel.BookDBViewModel
import com.habidev.bookdb.viewmodel.DetailInfoViewModel

class DetailFragment: Fragment(R.layout.detail) {
    private val bookDBViewModel: BookDBViewModel by activityViewModels()
    private val detailInfoViewModel: DetailInfoViewModel by activityViewModels()

    private lateinit var viewBinding: DetailBinding

    private var preventCommentListener: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DataBindingUtil.bind<DetailBinding>(view)?.let { binding ->
            viewBinding = binding

            viewBinding.viewModel = detailInfoViewModel
            viewBinding.lifecycleOwner = this
        }

        initViewListener()
    }

    override fun onStart() {
        super.onStart()

        preventCommentListener = true
    }

    private fun initViewListener() {
        Utils.setUpEditTextCloseKeyboard(requireActivity(), viewBinding.root)

        viewBinding.btnOpenInBrowser.setOnClickListener {
            val link: Uri = Uri.parse(detailInfoViewModel.link.value)
            val intent = Intent(Intent.ACTION_VIEW, link)
            startActivity(intent)
        }

        viewBinding.btnClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewBinding.editTextComment.addTextChangedListener { text ->
            if (!preventCommentListener) {
                detailInfoViewModel.bookItem.value?.let { bookItem ->
                    bookItem.comment = text?.toString()

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