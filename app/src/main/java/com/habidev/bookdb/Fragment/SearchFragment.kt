package com.habidev.bookdb.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.habidev.bookdb.Adapter.SearchViewPagerAdapter
import com.habidev.bookdb.database.BookViewModel
import com.habidev.bookdb.R
import com.habidev.bookdb.databinding.SearchBinding


class SearchFragment: Fragment() {
    private lateinit var viewBinding: SearchBinding

    private val bookViewModel: BookViewModel by activityViewModels()

    private lateinit var inputMethodManager: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = SearchBinding.inflate(inflater, container, false)

        inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SearchViewPagerAdapter(requireActivity())

        viewBinding.viewPagerSearch.adapter = adapter

        TabLayoutMediator(viewBinding.viewPagerTabLayoutSearch, viewBinding.viewPagerSearch) { tab, position ->
            when (position) {
                0 -> {
                    tab.icon = ContextCompat.getDrawable(requireContext(), R.drawable.collections_bookmark)
                    tab.text = "My Library"
                }
                1 -> {
                    tab.icon = ContextCompat.getDrawable(requireContext(), R.drawable.cloud)
                    tab.text = "Internet"
                }
            }
        }.attach()

        initListener()
    }

    override fun onResume() {
        super.onResume()

        if (viewBinding.editTextSearch.text.toString() == "") {
            viewBinding.editTextSearch.requestFocus()

            inputMethodManager.showSoftInput(viewBinding.editTextSearch, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun initListener() {
        viewBinding.btnSearch.setOnClickListener {
            inputMethodManager.hideSoftInputFromWindow(viewBinding.editTextSearch.windowToken, 0)

            val query: String = viewBinding.editTextSearch.text.toString()

            setSearchQuery(query)
        }

        viewBinding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query: String = viewBinding.editTextSearch.text.toString()

                setSearchQuery(query)
            }

            false
        }
    }

    private fun setSearchQuery(query: String) {
        if (query == "") {
            Toast.makeText(requireContext(), "Input Search Query", Toast.LENGTH_SHORT).show()
        } else {
            bookViewModel.searchQuery.value = query
        }
    }
}