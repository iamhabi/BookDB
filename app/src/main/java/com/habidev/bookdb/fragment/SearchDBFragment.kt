package com.habidev.bookdb.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
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

    private lateinit var items: MutableList<BookItem>
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

        items = mutableListOf()

        adapter = BookListAdapter(
            requireContext(),
            items,
            object : BookListAdapter.OnItemClickListener {
                override fun onClick(position: Int) {
                    val bookItem = items[position]
                    val intent = Intent(context, ResultActivity::class.java)
                    intent.putExtra("isbn", bookItem.isbn)
                    startActivity(intent)
                }

                override fun onLongClick(position: Int): Boolean {
                    // do nothing
                    return false
                }
            }
        )

        viewBinding.recyclerViewBase.adapter = adapter
        viewBinding.recyclerViewBase.layoutManager = LinearLayoutManager(context)
    }

    fun performSearch(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            requireActivity().runOnUiThread {
                clearResult()
            }

            val resultList = bookViewModel.search(query)

            for (bookItem in resultList) {
                requireActivity().runOnUiThread {
                    items.add(bookItem)
                    adapter.notifyItemInserted(adapter.itemCount - 1)
                }
            }
        }
    }

    fun clearResult() {
        adapter.notifyItemRangeRemoved(0, items.size)
        items.clear()
    }
}