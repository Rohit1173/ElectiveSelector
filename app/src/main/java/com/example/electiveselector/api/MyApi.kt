package com.example.electiveselector.api


import com.example.electiveselector.data.myResponse
import retrofit2.Call
import retrofit2.http.*

interface MyApi {
    @FormUrlEncoded
    @POST("/signup")
    fun createUser(
        @Field("Name") Name: String,
        @Field("userName") userName: String,
        @Field("userEmail") userEmail: String,
        @Field("userPassword") userPassword: String
    ): Call<myResponse>

    @FormUrlEncoded
    @POST("/login")
    fun loginUser(
        @Field("userName") userName: String,
        @Field("userPassword") userPassword: String
    ): Call<myResponse>

    @GET("/jwt")
    suspend fun verifyUser(
        @Header("auth-token") token:String
    ):myResponse

    @FormUrlEncoded
    @POST("/otp")
    fun sendOtp(
        @Field("userName") userName: String,
        @Field("userEmail") userEmail: String
    ): Call<myResponse>

    @FormUrlEncoded
    @POST("/profVerify")
    fun profVerify(
        @Field("userEmail")userEmail: String
    ):Call<myResponse>
}