package com.habidev.bookdb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.habidev.bookdb.databinding.BookListBinding

class BookListFragment: Fragment() {
    private lateinit var viewBinding: BookListBinding
    private lateinit var bookViewModel: BookViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = BookListBinding.inflate(inflater, container, false)
        bookViewModel = BookViewModel()

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = bookViewModel.bookItemList

        val adapter = BookAdapter(requireContext(), items)

        viewBinding.bookRecyclerView.adapter = adapter
    }
}