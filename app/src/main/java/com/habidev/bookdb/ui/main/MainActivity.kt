package com.habidev.bookdb.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.habidev.bookdb.R
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.data.GroupItem
import com.habidev.bookdb.database.BooksApplication
import com.habidev.bookdb.databinding.ActivityMainBinding
import com.habidev.bookdb.ui.CameraFragment
import com.habidev.bookdb.ui.DetailFragment
import com.habidev.bookdb.ui.ResultFragment
import com.habidev.bookdb.ui.book.BookListFragment
import com.habidev.bookdb.ui.group.GroupListFragment
import com.habidev.bookdb.ui.search.SearchFragment
import com.habidev.bookdb.utils.Utils
import com.habidev.bookdb.viewmodel.BookDBViewModel
import com.habidev.bookdb.viewmodel.BookViewModelFactory
import com.habidev.bookdb.viewmodel.DetailInfoViewModel
import com.habidev.bookdb.viewmodel.SearchViewModel


interface SomeInterface {
    fun showDetailInfo(bookItem: BookItem)
    fun showResultInfo(query: String)
}

class MainActivity : AppCompatActivity(), SomeInterface {
    private val bookDBViewModel: BookDBViewModel by viewModels {
        BookViewModelFactory((application as BooksApplication).repository)
    }

    private val searchViewModel: SearchViewModel by viewModels()
    private val detailInfoViewModel: DetailInfoViewModel by viewModels()

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

        bookDBViewModel.init()

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

        groupListFragment.setOnGroupListener(object : GroupListFragment.OnGroupListener {
            override fun onAllSelected() {
                bookListFragment.updateAllBooks()
            }

            override fun onGroupSelected(groupItem: GroupItem) {
                bookListFragment.updateBooksByGroup(groupItem)
            }
        })
    }

    private fun initViewListener() {
        viewBinding.btnSearch.setOnClickListener {
            showFrag(searchFragment)
        }

        viewBinding.btnMore.setOnClickListener {
            showFrag(groupListFragment, slideFromRight = false)
        }

        viewBinding.btnOpenCamera.setOnClickListener {
            if (Utils.isCamPermissionGranted(this)) {
                showFrag(cameraFragment)
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
                val uri = Uri.fromParts("package", packageName, null)

                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    data = uri
                }

                startActivity(intent)
            }
            .setNegativeButton(R.string.permission_dialog_neg_button) { _, _ ->
                // do nothing
            }
            .create()

        dialog.show()
    }

    private fun showFrag(frag: Fragment, slideFromRight: Boolean = true) {
        supportFragmentManager.commit {
            if (slideFromRight) {
                setCustomAnimations(
                    R.anim.slide_in_from_right,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out_to_right
                )
            } else {
                setCustomAnimations(
                    R.anim.slide_in_from_left,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out_to_left
                )
            }

            add(viewBinding.frameLayoutFull.id, frag)

            addToBackStack(null)
        }
    }

    override fun showDetailInfo(bookItem: BookItem) {
        detailInfoViewModel.setBookItem(bookItem)

        showFrag(detailFragment)
    }

    override fun showResultInfo(query: String) {
        resultFragment.setQuery(query)

        showFrag(resultFragment)
    }
}