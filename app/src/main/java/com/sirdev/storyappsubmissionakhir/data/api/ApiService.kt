package com.sirdev.storyappsubmissionakhir.data.api

import com.sirdev.storyappsubmissionakhir.data.response.DetailUserStoryResponse
import com.sirdev.storyappsubmissionakhir.data.response.GeneralResponseError
import com.sirdev.storyappsubmissionakhir.data.response.ResponseLoginUser
import com.sirdev.storyappsubmissionakhir.data.response.ResponseUserStories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun userRegister(
        @Field("name") Name: String,
        @Field("email") Email: String,
        @Field("password") Password: String
    ): Call<GeneralResponseError>

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email") Email: String,
        @Field("password") Password: String
    ): Call<ResponseLoginUser>

    @GET("stories")
    suspend fun getAllUserStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): ResponseUserStories

    @GET("stories")
    fun getAllUserStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 0
    ): Call<ResponseUserStories>

    @GET("stories/{id}")
    fun detailUserStory(
        @Header("Authorization") token: String,
        @Query("id") id: String
    ): Call<DetailUserStoryResponse>

    @Multipart
    @POST("stories")
    fun uploadNewStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<GeneralResponseError>
}