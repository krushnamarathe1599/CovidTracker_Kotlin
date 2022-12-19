package com.example.covidtracker_kotlin

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("countries")
    fun getCountryData(): Call<List<ModelClass>>

}