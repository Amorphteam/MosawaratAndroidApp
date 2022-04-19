package com.aj.hajarialmustafa.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator {
    companion object{
        private val retrofitBuilder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://al-mostafa.co/makhtot/wp-json/fr-api/v1/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = retrofitBuilder.build()
        val requestApi:RequestApi = retrofit.create(RequestApi::class.java)
    }
}