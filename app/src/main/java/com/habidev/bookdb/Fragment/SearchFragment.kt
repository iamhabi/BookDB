package com.habidev.bookdb.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.habidev.bookdb.Activity.DetailActivity
import com.habidev.bookdb.BookItem
import com.habidev.bookdb.BookListAdapter
import com.habidev.bookdb.BookViewModel
import com.habidev.bookdb.DevKey
import com.habidev.bookdb.databinding.SearchBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class SearchFragment: Fragment() {
    private lateinit var viewBinding: SearchBinding

    private val bookViewModel: BookViewModel by activityViewModels()

    private val dbResultList: MutableList<BookItem> = mutableListOf()
    private val internetResultList: MutableList<BookItem> = mutableListOf()

    private lateinit var dbListAdapter: BookListAdapter
    private lateinit var internetListAdapter: BookListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = SearchBinding.inflate(inflater, container, false)

        // TODO
        //  Request Focus to Edit Text
        //  show keyboard

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DB Search Result Recycler View
        dbListAdapter = BookListAdapter(requireContext(), dbResultList, dbOnItemClickListener)

        viewBinding.recyclerViewDbResult.adapter = dbListAdapter
        viewBinding.recyclerViewDbResult.layoutManager = LinearLayoutManager(context)

        // Internet Search Result Recycler View
        internetListAdapter = BookListAdapter(requireContext(), internetResultList, internetOnItemClickListener)

        viewBinding.recyclerViewInternetResult.adapter = internetListAdapter
        viewBinding.recyclerViewInternetResult.layoutManager = LinearLayoutManager(context)

        initListener()
    }

    private fun initListener() {
        viewBinding.btnSearch.setOnClickListener {
            // TODO
            //  hide keyboard
            //  Remove Focus from Edit Text

            val query: String = viewBinding.editTextSearch.text.toString()

            if (query == "") {
                Toast.makeText(requireContext(), "Input Search Query", Toast.LENGTH_SHORT).show()
            } else {
                viewBinding.textViewDbResult.visibility = View.VISIBLE

                bookViewModel.search(query).observe(requireActivity()) { resultList ->
                    resultList.let {
                        dbResultList.clear()
                        dbResultList.addAll(resultList)

                        dbListAdapter.notifyItemRangeInserted(0, dbResultList.size)
                    }
                }

                getInfoFromNaverAndShow(query)
            }
        }
    }

    private fun jsonArrayToBookItemList(resultJson: String): List<BookItem> {
        val resultJsonArray = JSONObject(resultJson).getJSONArray("items")

        Log.d("Result", resultJsonArray.toString())

        val bookItemList = mutableListOf<BookItem>()

        for (i in 0 until resultJsonArray.length()) {
            val jsonObject = resultJsonArray.getJSONObject(i)

            val isbn = jsonObject.get("isbn") as String
            val link = jsonObject.get("link") as String
            val title = jsonObject.get("title") as String
            val author = jsonObject.get("author") as String
            val imageUrl = jsonObject.get("image") as String
            val description = jsonObject.get("description") as String

            bookItemList.add(
                BookItem(isbn.toLong(), link, title, author, imageUrl, description)
            )
        }

        return bookItemList
    }

    private fun getInfoFromNaverAndShow(query: String) {
        Thread {
            try {
                val con = connect("${DevKey.URL}$query")

                con?.requestMethod = "GET"
                con?.setRequestProperty("X-Naver-Client-Id", DevKey.CLIENT_ID)
                con?.setRequestProperty("X-Naver-Client-Secret", DevKey.CLIENT_SECRET)

                val responseCode = con!!.responseCode
                val result = if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                    readBody(con.inputStream)
                } else { // 에러 발생
                    readBody(con.errorStream)
                }

                val bookItemList = jsonArrayToBookItemList(result)

                internetResultList.clear()
                internetResultList.addAll(bookItemList)

                Handler(Looper.getMainLooper()).post {
                    viewBinding.textViewInternetResult.visibility = View.VISIBLE

                    internetListAdapter.notifyItemRangeInserted(0, internetResultList.size)
                }
            } catch (e: Exception) {
                Log.e("FAIL", e.toString())
            }
        }.start()
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

    private val dbOnItemClickListener = object: BookListAdapter.OnItemClickListener {
        override fun onClick(position: Int) {
            val bookItem = dbResultList[position]

            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("bookItem", bookItem)
            startActivity(intent)
        }

        override fun onLongClick(position: Int): Boolean {
            return true
        }
    }

    private val internetOnItemClickListener = object: BookListAdapter.OnItemClickListener {
        override fun onClick(position: Int) {
            val bookItem = internetResultList[position]

            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("bookItem", bookItem)
            startActivity(intent)
        }

        override fun onLongClick(position: Int): Boolean {
            return true
        }
    }
}