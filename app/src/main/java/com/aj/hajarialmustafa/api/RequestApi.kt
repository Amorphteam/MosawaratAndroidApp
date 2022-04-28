package com.aj.hajarialmustafa.api

import com.aj.hajarialmustafa.model.MakhtotItem
import com.aj.hajarialmustafa.model.Post
import com.aj.hajarialmustafa.model.Update
import io.reactivex.Flowable
import retrofit2.http.GET

interface RequestApi {
    @GET("posts")
    fun getAllMakhtotItems(): Flowable<MakhtotItem>

    @GET("last-update")
    fun getJsonLastUpdateDate(): Flowable<Update>
}