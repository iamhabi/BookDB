package com.habidev.bookdb

import androidx.lifecycle.ViewModel

class BookViewModel: ViewModel() {
     var bookItemList: List<BookItem>
        get() = bookItemList
        set(bookItemList) {
            this.bookItemList = bookItemList
        }
}