package com.aj.hajarialmustafa.api

import com.aj.hajarialmustafa.model.MakhtotItem
import io.reactivex.Flowable
import retrofit2.http.GET

interface RequestApi {
    @GET("posts")
    fun getAllMakhtotItems(): Flowable<List<MakhtotItem>>
}