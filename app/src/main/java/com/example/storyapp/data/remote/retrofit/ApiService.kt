package com.example.storyapp.data.remote.retrofit

import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : RegisterResponse


    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

}