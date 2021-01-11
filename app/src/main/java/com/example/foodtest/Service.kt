package com.example.foodtest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Service{
    @GET("/mealServiceDietInfo")
    fun ApiService(
        @Query("KEY") key : String = "bde3d1d967c544409a90eab8b28d4aec",
        @Query("ATPT_OFCDC_SC_CODE") region : String = "F10",
        @Query("SD_SCHUL_CODE") school : Int = 7380292,
        @Query("MMEAL_SC_CODE") menu : Int = 2,
        @Query("Type") type : String = "json"
    ) : Call<Meal>
}