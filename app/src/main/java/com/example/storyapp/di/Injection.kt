    package com.example.storyapp.di

    import android.content.Context
    import com.example.storyapp.data.remote.retrofit.ApiConfig
    import com.example.storyapp.repository.UserRepository

    object Injection {
        fun provideRepository(context: Context): UserRepository {
            val apiService = ApiConfig.getApiService()
            return UserRepository.getInstance(apiService)
        }

    }