package com.example.covidtracker_kotlin

data class ModelClass(
    var cases: String,
    var todayCases: String,
    var deaths: String,
    var todayDeaths: String,
    var recovered: String,
    var todayRecovered: String,
    var active: String,
    var country: String,
)