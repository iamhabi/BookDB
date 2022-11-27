package com.habidev.bookdb

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.habidev.bookdb.database.BookDao
import com.habidev.bookdb.database.BookDatabase
import com.habidev.bookdb.databinding.BookResultBinding
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class ResultActivity: AppCompatActivity() {
    private val tag: String = "Result Activity"

    private lateinit var viewBinding: BookResultBinding

    private lateinit var bookItem: BookItem
    private lateinit var bookDao: BookDao

    private lateinit var item: JSONObject

    private var barcode: Int = 0
    private lateinit var imageUrl: String
    private lateinit var title: String
    private lateinit var author: String
    private lateinit var link: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = BookResultBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initBookDao()
        initOnClickListener()

        barcode = getBarcodeFromBundle()
        val resultJson = readJsonFromBundle()

        if (resultJson != null) {
            getInfoFromJsonObject(resultJson)
            setInfo()
        } else {
            Log.e(tag, "Result is null")
        }
    }

    private fun initBookDao() {
        val database = Room.databaseBuilder(
            this,
            BookDatabase::class.java, "books"
        ).build()

        bookDao = database.bookDao()
    }

    private fun getBarcodeFromBundle(): Int {
        val bundle = intent.extras

        return bundle?.getInt("barcode") ?: -1
    }

    private fun readJsonFromBundle(): String? {
        val bundle = intent.extras

        return bundle?.getString("result")
    }

    private fun getInfoFromJsonObject(resultJson: String) {
        item = JSONObject(resultJson).getJSONArray("items").getJSONObject(0)

        title = item.get("title") as String
        author = item.get("author") as String
        imageUrl = item.get("image") as String

        link = item.get("link") as String
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
    }

    private fun buildBookItem() {
        bookItem = BookItem(barcode, imageUrl, title, author, link)
    }

    private fun addToDatabase() {
        runBlocking {
            bookDao.insertBook(bookItem)
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun initOnClickListener() {
        viewBinding.btnOpenInBrowser.setOnClickListener {
            val linkUri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, linkUri)
            startActivity(intent)
        }

        viewBinding.btnAddBookmark.setOnClickListener {
            buildBookItem()
            addToDatabase()
        }
    }
}