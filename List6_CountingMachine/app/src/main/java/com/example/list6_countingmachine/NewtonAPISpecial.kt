package com.example.list6_countingmachine

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NewtonAPISpecial {
    @GET("/{operation}/{expression}")
    fun getResultOf(@Path("operation") operation : String, @Path("expression") expression: String) : Call<NewtonObjectSpecial>
}