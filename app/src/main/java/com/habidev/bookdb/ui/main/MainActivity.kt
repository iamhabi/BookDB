package com.habidev.bookdb.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.habidev.bookdb.R
import com.habidev.bookdb.SearchViewModel
import com.habidev.bookdb.database.BookItem
import com.habidev.bookdb.database.BookViewModel
import com.habidev.bookdb.database.BookViewModelFactory
import com.habidev.bookdb.database.BooksApplication
import com.habidev.bookdb.databinding.ActivityMainBinding
import com.habidev.bookdb.ui.CameraFragment
import com.habidev.bookdb.ui.DetailFragment
import com.habidev.bookdb.ui.ResultFragment
import com.habidev.bookdb.ui.book.BookListFragment
import com.habidev.bookdb.ui.group.GroupListFragment
import com.habidev.bookdb.ui.search.SearchFragment
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

    private val searchViewModel: SearchViewModel by viewModels()

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isEmpty()) {
            return
        }

        val result = grantResults[0]

        if (requestCode == Utils.PERMISSION_CAMERA_REQUEST_CODE && result != PackageManager.PERMISSION_GRANTED) {
            showDialogMoveToSetting()
        }
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
            if (Utils.isCamPermissionGranted(this)) {
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
            } else {
                Utils.requestCameraPermission(this)
            }
        }
    }

    private fun showDialogMoveToSetting() {
        val dialog = AlertDialog.Builder(this, R.style.DialogStyle)
            .setTitle(R.string.permission_dialog_title)
            .setMessage(R.string.permission_dialog_message)
            .setPositiveButton(R.string.permission_dialog_pos_button) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                val uri = Uri.fromParts("package", packageName, null)

                intent.setData(uri)

                startActivity(intent)
            }
            .setNegativeButton(R.string.permission_dialog_neg_button) { _, _ ->
                // do nothing
            }
            .create()

        dialog.show()
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