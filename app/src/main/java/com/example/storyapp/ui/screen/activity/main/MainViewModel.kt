package com.example.storyapp.ui.screen.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.LoginResult
import com.example.storyapp.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> =
        repository.getStory().cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }
}