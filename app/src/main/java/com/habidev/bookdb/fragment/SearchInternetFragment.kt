package com.habidev.bookdb.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.habidev.bookdb.ApiKey
import com.habidev.bookdb.activity.ResultActivity
import com.habidev.bookdb.adapter.BookListAdapter
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.databinding.RecyclerViewBaseBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class SearchInternetFragment : Fragment() {
    companion object {
        private const val TAG = "SearchInternet"
    }

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
            try {
                val con = connect("${ApiKey.URL}$query")

                con?.requestMethod = "GET"
                con?.setRequestProperty("X-Naver-Client-Id", ApiKey.CLIENT_ID)
                con?.setRequestProperty("X-Naver-Client-Secret", ApiKey.CLIENT_SECRET)

                val responseCode = con!!.responseCode
                val result = if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                    readBody(con.inputStream)
                } else { // 에러 발생
                    readBody(con.errorStream)
                }

                // TODO
                //  search result is only 10

                requireActivity().runOnUiThread {
                    adapter.notifyItemRangeRemoved(0, items.size)
                    items.clear()
                }

                showResult(result)
            } catch (e: Exception) {
                Log.e("FAIL", e.toString())
            }
        }
    }

    private fun showResult(resultJson: String) {
        val resultJsonArray = JSONObject(resultJson).getJSONArray("items")

        for (i in 0 until resultJsonArray.length()) {
            val jsonObject = resultJsonArray.getJSONObject(i)

            val isbn = jsonObject.get("isbn") as String
            val link = jsonObject.get("link") as String
            val title = jsonObject.get("title") as String
            val author = jsonObject.get("author") as String
            val imageUrl = jsonObject.get("image") as String
            val description = jsonObject.get("description") as String

            val bookItem = BookItem(
                isbn.toLong(), link, title, author, imageUrl, description, null, 0,
                isOwning = false,
                wannaBuy = false
            )

            requireActivity().runOnUiThread {
                items.add(bookItem)
                adapter.notifyItemInserted(adapter.itemCount - 1)
            }
        }
    }

    private fun connect(apiUrl: String): HttpURLConnection? {
        return try {
            val url = URL(apiUrl)
            url.openConnection() as HttpURLConnection
        } catch (e: MalformedURLException) {
            throw RuntimeException("API URL이 잘못되었습니다. : $apiUrl", e)
        } catch (e: IOException) {
            throw RuntimeException("연결이 실패했습니다. : $apiUrl", e)
        }
    }

    private fun readBody(body: InputStream): String {
        val streamReader = InputStreamReader(body)
        try {
            BufferedReader(streamReader).use { lineReader ->
                val responseBody = StringBuilder()
                var line: String?
                while (lineReader.readLine().also { line = it } != null) {
                    responseBody.append(line)
                }
                return responseBody.toString()
            }
        } catch (e: IOException) {
            throw RuntimeException("API 응답을 읽는데 실패했습니다.", e)
        }
    }
}
