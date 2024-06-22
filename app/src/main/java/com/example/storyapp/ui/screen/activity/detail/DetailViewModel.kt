package com.example.storyapp.ui.screen.activity.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.remote.response.DetailStoryResponse
import com.example.storyapp.repository.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: UserRepository,
    id: String
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailStory = MutableLiveData<DetailStoryResponse>()
    val detailStory: LiveData<DetailStoryResponse> = _detailStory

    init {
        getDetailStory(id)
    }

    private fun getDetailStory(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val detailStory = repository.getDetailStory(id)
                _detailStory.value = detailStory
                Log.d("DetailViewModel", "Detail story: $detailStory")
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}