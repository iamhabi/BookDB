package com.habidev.bookdb.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.habidev.bookdb.activity.ResultActivity
import com.habidev.bookdb.adapter.BookListAdapter
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.database.BookViewModel
import com.habidev.bookdb.databinding.RecyclerViewBaseBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchDBFragment : Fragment() {
    companion object {
        private const val TAG = "SearchDB"
    }

    private val bookViewModel: BookViewModel by activityViewModels()

    private lateinit var viewBinding: RecyclerViewBaseBinding

    private lateinit var adapter: BookListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = RecyclerViewBaseBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = BookListAdapter(requireContext())

        adapter.setOnItemClickListener(object : BookListAdapter.OnItemClickListener {
            override fun onClick(position: Int, bookItem: BookItem) {
                val intent = Intent(context, ResultActivity::class.java)
                intent.putExtra("isbn", bookItem.isbn)
                startActivity(intent)
            }

            override fun onMoreClick(position: Int, bookItem: BookItem) {
            }
        })

        viewBinding.recyclerViewBase.adapter = adapter
        viewBinding.recyclerViewBase.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    fun performSearch(query: String) {
        adapter.clear()

        if (query == "") {
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val resultList = bookViewModel.search(query)

            CoroutineScope(Dispatchers.Main).launch {
                adapter.add(resultList)
            }
        }
    }
}