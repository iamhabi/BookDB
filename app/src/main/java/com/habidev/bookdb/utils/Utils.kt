package com.habidev.bookdb.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class Utils {
    companion object {
        const val TAG = "BookDB"

        fun showKeyboard(context: Context, view: View) {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            inputMethodManager.showSoftInput(
                view,
                InputMethodManager.SHOW_IMPLICIT
            )
        }

        fun hideKeyboard(context: Context, view: View) {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}