package com.habidev.bookdb.activity

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.habidev.bookdb.R
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.database.BookViewModel
import com.habidev.bookdb.database.BookViewModelFactory
import com.habidev.bookdb.database.BooksApplication
import com.habidev.bookdb.databinding.ActivityMainBinding
import com.habidev.bookdb.fragment.BookListFragment
import com.habidev.bookdb.fragment.CameraFragment
import com.habidev.bookdb.fragment.DetailFragment
import com.habidev.bookdb.fragment.GroupListFragment
import com.habidev.bookdb.fragment.ResultFragment
import com.habidev.bookdb.fragment.SearchFragment
import com.habidev.bookdb.utils.Utils


interface SomeInterface {
    fun showDetailInfo(bookItem: BookItem)
    fun showResultInfo(query: String)
}

class MainActivity : AppCompatActivity(), SomeInterface {
    companion object {
        private const val TAG = "BookDBMainAct"
    }

    private val bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((application as BooksApplication).repository)
    }

    private val bookListFragment = BookListFragment()
    private val groupListFragment = GroupListFragment()
    private val searchFragment = SearchFragment()
    private val cameraFragment = CameraFragment()
    private val detailFragment = DetailFragment()
    private val resultFragment = ResultFragment()

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        bookViewModel.create()

        initBookList()
        initViewListener()
    }

    private fun initBookList() {
        supportFragmentManager.beginTransaction()
            .add(viewBinding.frameLayoutBookList.id, bookListFragment)
            .commit()
    }

    private fun initViewListener() {
        viewBinding.btnSearch.setOnClickListener {
            supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.slide_in_from_right,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out_to_right
                )

                add(viewBinding.frameLayoutFull.id, searchFragment)

                addToBackStack(null)
            }
        }

        viewBinding.btnMore.setOnClickListener {
            supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.slide_in_from_left,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out_to_left
                )

                add(viewBinding.frameLayoutFull.id, groupListFragment)

                addToBackStack(null)
            }
        }

        viewBinding.btnOpenCamera.setOnClickListener {
            supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.slide_in_from_right,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out_to_right
                )

                add(viewBinding.frameLayoutFull.id, cameraFragment)

                addToBackStack(null)
            }
        }
    }

    override fun showDetailInfo(bookItem: BookItem) {
        detailFragment.setBookItem(bookItem)

        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out_to_right
            )

            add(viewBinding.frameLayoutFull.id, detailFragment)

            addToBackStack(null)
        }
    }

    override fun showResultInfo(query: String) {
        resultFragment.setQuery(query)

        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out_to_right
            )

            add(viewBinding.frameLayoutFull.id, resultFragment)

            addToBackStack(null)
        }
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent?): Boolean {
        motionEvent?.let { event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val view = currentFocus
                if (view is EditText) {
                    val outRect = Rect()

                    view.getGlobalVisibleRect(outRect)

                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        view.clearFocus()
                        Utils.hideKeyboard(this@MainActivity, view)
                    }
                }
            }
        }

        return super.dispatchTouchEvent(motionEvent)
    }
}