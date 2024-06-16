    package com.example.storyapp.di

    import android.content.Context
    import com.example.storyapp.data.remote.retrofit.ApiConfig
    import com.example.storyapp.repository.StroryAppRepository

    object Injection {
        fun provideRepository(context: Context): StroryAppRepository {
            val apiService = ApiConfig.getApiService()
            return StroryAppRepository.getInstance(apiService)
        }

    }