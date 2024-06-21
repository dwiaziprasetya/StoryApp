package com.example.storyapp.ui.screen.activity.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.repository.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: UserRepository,
    private val id: String
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailStory = MutableLiveData<StoryResponse>()
    val detailStory: LiveData<StoryResponse> = _detailStory

    init {
        getDetailStory(id)
    }

    private fun getDetailStory(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val detailStory = repository.getDetailStory(id)
                _detailStory.value = detailStory
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}