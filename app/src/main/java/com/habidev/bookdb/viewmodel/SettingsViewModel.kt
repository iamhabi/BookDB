package com.habidev.bookdb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.habidev.bookdb.data.SettingsItem
import com.habidev.bookdb.database.SettingsRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
): ViewModel() {
    fun init() = viewModelScope.launch {
        settingsRepository.init()
    }

    val settings: LiveData<SettingsItem> = settingsRepository.settings.asLiveData()

    fun update(settingsItem: SettingsItem) = viewModelScope.launch {
        settingsRepository.update(settingsItem)
    }
}

class SettingsViewModelFactory(
    private val settingsRepository: SettingsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingsRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}