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
import com.habidev.bookdb.databinding.BookDetailBinding
import org.json.JSONObject

class BookDetailActivity: AppCompatActivity() {
    private val TAG: String = "Book Activity"

    private lateinit var viewBinding: BookDetailBinding

    private lateinit var bookItem: BookItem
    private lateinit var bookDao: BookDao

    private lateinit var item: JSONObject

    private var barcode: Int = 0
    private lateinit var imageUrl: String
    private lateinit var title: String
    private lateinit var author: String
    private lateinit var link: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = BookDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initBookDao()
        initOnClickListener()

        barcode = getBarcodeFromBundle()
        val resultJson = readJsonFromBundle()

        if (resultJson != null) {
            getInfoFromJsonObject(resultJson)
            setInfo()
        } else {
            Log.e(TAG, "Result is null")
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

        val linkString = item.get("link") as String
        link = Uri.parse(linkString)
    }

    private fun setInfo() {
        Glide
            .with(this)
            .load(Uri.parse(imageUrl))
            .placeholder(R.drawable.book)
            .error(R.drawable.book)
            .into(viewBinding.detailImage)

        viewBinding.detailTitle.text = title
        viewBinding.detailAuthor.text = author
    }

    private fun buildBookItem() {
        bookItem = BookItem(barcode, imageUrl, title, author)
    }

    private fun addToDatabase() {
        bookDao.insertBook(bookItem)
    }

    private fun initOnClickListener() {
        viewBinding.btnOpenInBrowser.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, link)
            startActivity(intent)
        }

        viewBinding.btnAddBookmark.setOnClickListener {
            buildBookItem()
            addToDatabase()
        }
    }
}