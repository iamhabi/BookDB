package com.habidev.bookdb.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.habidev.bookdb.adapter.SimpleViewPagerAdapter
import com.habidev.bookdb.databinding.SearchBinding
import com.habidev.bookdb.utils.Utils

class SearchFragment : Fragment() {
    companion object {
        private const val TAG = "Search"
    }

    private lateinit var viewBinding: SearchBinding

    private lateinit var searchDBFrag: SearchDBFragment
    private lateinit var searchInternetFrag: SearchInternetFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = SearchBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()

        initListener()
    }

    override fun onResume() {
        super.onResume()

        if (viewBinding.editTextSearch.text.toString() == "") {
            viewBinding.editTextSearch.requestFocus()

            Utils.showKeyboard(requireContext(), viewBinding.editTextSearch)
        }
    }

    private fun initViewPager() {
        searchDBFrag = SearchDBFragment()
        searchInternetFrag = SearchInternetFragment()

        val fragments = arrayListOf(
            searchDBFrag,
            searchInternetFrag
        )

        val adapter = SimpleViewPagerAdapter(requireActivity(), fragments)

        viewBinding.viewPagerSearch.adapter = adapter
        viewBinding.viewPagerSearch.offscreenPageLimit = fragments.size

        TabLayoutMediator(viewBinding.tabLayoutSearch, viewBinding.viewPagerSearch) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Database"
                }

                1 -> {
                    tab.text = "Internet"
                }
            }
        }.attach()
    }

    private fun performSearch(query: String) {
        searchDBFrag.performSearch(query)
        searchInternetFrag.performSearch(query)
    }

    private fun initListener() {
        viewBinding.btnSearch.setOnClickListener {
            Utils.hideKeyboard(requireContext(), viewBinding.editTextSearch)

            val query: String = viewBinding.editTextSearch.text.toString()

            performSearch(query)
        }

        viewBinding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                performSearch(query.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        viewBinding.editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                Utils.hideKeyboard(requireContext(), view)
            }
        }
    }
}