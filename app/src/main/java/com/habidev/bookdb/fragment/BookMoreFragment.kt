package com.habidev.bookdb.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.habidev.bookdb.databinding.BookListMoreBinding

class BookMoreFragment : BottomSheetDialogFragment() {
    companion object {
        private const val TAG = "BookMore"
    }

    private lateinit var viewBinding: BookListMoreBinding

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

    private fun initViewListener() {
        viewBinding.textViewMoveToLink.setOnClickListener {
            Log.d(TAG, "move to link")
        }

        viewBinding.layoutReadingState.setOnClickListener {
            Log.d(TAG, "Reading state")
        }

        viewBinding.layoutOwnState.setOnClickListener {
            Log.d(TAG, "Own state")
        }
    }
}