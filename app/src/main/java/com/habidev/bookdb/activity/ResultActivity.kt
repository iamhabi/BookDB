package com.habidev.bookdb.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.habidev.bookdb.*
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.database.BookViewModel
import com.habidev.bookdb.database.BookViewModelFactory
import com.habidev.bookdb.database.BooksApplication
import com.habidev.bookdb.databinding.ResultBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ResultActivity: AppCompatActivity() {
    private lateinit var viewBinding: ResultBinding

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((application as BooksApplication).repository)
    }

    private lateinit var resultJsonObject: JSONObject

    private var isbn: Long = -1
    private lateinit var link: String
    private lateinit var title: String
    private lateinit var author: String
    private lateinit var imageUrl: String
    private lateinit var description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ResultBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initListener()

        val bundle = intent.extras

        val barcode = bundle?.getString("barcode") ?: ""

        if (barcode != "") {
            getInfoFromNaverAndShow(barcode)
        } else {
            Toast.makeText(this, "Invalid Barcode", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getInfoFromJsonObject(resultJson: String) {
        resultJsonObject = JSONObject(resultJson).getJSONArray("items").getJSONObject(0)

        isbn = (resultJsonObject.get("isbn") as String).toLong()
        link = resultJsonObject.get("link") as String
        title = resultJsonObject.get("title") as String
        author = resultJsonObject.get("author") as String
        imageUrl = resultJsonObject.get("image") as String
        description = resultJsonObject.get("description") as String
    }

    private fun setInfo() {
        Glide
            .with(this)
            .load(Uri.parse(imageUrl))
            .placeholder(R.drawable.book)
            .error(R.drawable.book)
            .into(viewBinding.resultImage)

        viewBinding.resultTitle.text = title
        viewBinding.resultAuthor.text = author
        viewBinding.resultDescription.text = description
    }

    private fun addToDatabase() {
        bookViewModel.insert(
            BookItem(isbn, link, title, author, imageUrl, description)
        )

        finish()
    }

    private fun initListener() {
        viewBinding.btnOpenInBrowser.setOnClickListener {
            val linkUri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, linkUri)
            startActivity(intent)
        }

        viewBinding.btnAddBookmark.setOnClickListener {
            addToDatabase()
        }
    }

    private fun getInfoFromNaverAndShow(barcode: String) {
        Thread {
            try {
                val con = connect("${ApiKey.URL}$barcode")

                con?.requestMethod = "GET"
                con?.setRequestProperty("X-Naver-Client-Id", ApiKey.CLIENT_ID)
                con?.setRequestProperty("X-Naver-Client-Secret", ApiKey.CLIENT_SECRET)

                val responseCode = con!!.responseCode
                val result = if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                    readBody(con.inputStream)
                } else { // 에러 발생
                    readBody(con.errorStream)
                }

                getInfoFromJsonObject(result)

                Handler(Looper.getMainLooper()).post {
                    setInfo()
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
}