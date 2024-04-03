package com.habidev.bookdb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.habidev.bookdb.api.BookDBClient
import com.habidev.bookdb.data.BookItem
import com.habidev.bookdb.data.GroupBookItem
import com.habidev.bookdb.data.GroupItem
import com.habidev.bookdb.database.BookRepository
import kotlinx.coroutines.launch

class BookDBViewModel(private val repository: BookRepository): ViewModel() {
    fun init() {
        BookDBClient.getList { books ->
            _allBooks.value = books as MutableList<BookItem>
        }

        BookDBClient.getGroups { groups ->
            _groups.value = groups as MutableList<GroupItem>
        }
    }

    private val _allBooks: MutableLiveData<MutableList<BookItem>> = MutableLiveData(mutableListOf())
    private val _groups: MutableLiveData<MutableList<GroupItem>> = MutableLiveData(mutableListOf())

    val allBooks: LiveData<MutableList<BookItem>> = _allBooks
    val groups: LiveData<MutableList<GroupItem>> = _groups

    val allBooksLiveData: LiveData<List<BookItem>> = repository.allBooksFlow.asLiveData()
    val allGroupsLiveData: LiveData<List<GroupItem>> = repository.allGroupsFlow.asLiveData()

    fun booksByGroupLiveData(groupItem: GroupItem): LiveData<List<BookItem>> = repository.booksByGroupFlow(groupItem).asLiveData()

    suspend fun insertBook(bookItem: BookItem): Boolean = repository.insertBook(bookItem)

    fun insertBookIntoGroup(bookItem: BookItem, groupItem: GroupItem) = viewModelScope.launch {
        repository.insertBookIntoGroup(bookItem.isbn, groupItem.id)
    }

    fun insertGroup(groupItem: GroupItem) = viewModelScope.launch {
        repository.insertGroup(groupItem)
    }

    fun updateBook(bookItem: BookItem) = viewModelScope.launch {
        repository.updateBook(bookItem)
    }

    fun updateGroup(groupItem: GroupItem) = viewModelScope.launch {
        repository.updateGroup(groupItem)
    }

    fun deleteBook(bookItem: BookItem) = viewModelScope.launch {
        repository.deleteBook(bookItem)
    }

    fun deleteBookFromGroup(groupBookItem: GroupBookItem) = viewModelScope.launch {
        repository.deleteBookFromGroup(groupBookItem)
    }

    fun deleteGroup(groupItem: GroupItem) = viewModelScope.launch {
        repository.deleteGroup(groupItem)
    }

    fun searchBook(query: String): List<BookItem> = repository.searchBook(query)
}

class BookViewModelFactory(private val repository: BookRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDBViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookDBViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}