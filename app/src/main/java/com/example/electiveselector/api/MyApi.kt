package com.example.electiveselector.api


import com.example.electiveselector.data.*
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

   // @FormUrlEncoded
    @POST("/addElectiveDetails")
    fun addElectiveDetails(
        @Body electiveData: ElectiveData
    ):Call<myResponse>

    @GET("/announcement")
    suspend fun announcements(): AnnouncementResponse

    @FormUrlEncoded
    @POST("/sem")
    fun semData(
        @Field("semNum")semNum: String,
        @Field("userEmail")userEmail:String
    ):Call<semResponse>

    @FormUrlEncoded
    @POST("/choose")
    fun chooseSub(
        @Field("userEmail")userEmail: String,
        @Field("semNum")semNum: String,
        @Field("electiveNum")electiveNum: String,
        @Field("choiceString")choiceString: String
    ):Call<myResponse>


    @FormUrlEncoded
    @POST("/selectedElectives")
    fun getSelectedElectives(
        @Field("userEmail")userEmail: String,
    ):Call<selectedElectivesData>
}