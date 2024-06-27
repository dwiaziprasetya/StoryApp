package com.example.storyapp.ui.screen.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.remote.response.LoginResult
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _stories = MutableLiveData<StoryResponse>()
    val stories: LiveData<StoryResponse> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getStories()
    }

    fun getStories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val story = repository.getStories()
                _stories.value = story
            } catch (e: Exception) {
                _stories.value = StoryResponse(
                    error = true,
                    message = e.message.toString(),
                    listStory = emptyList()
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }
}