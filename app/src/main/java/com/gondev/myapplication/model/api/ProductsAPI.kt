package com.gondev.myapplication.model.api

import com.gondev.myapplication.model.data.ProductListResponse
import com.gondev.statemanager.state.Status
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsAPI {
    @GET("api/home")
    suspend fun requestGetFirstProductList(): Status<ProductListResponse>

    @GET("api/home/goods")
    suspend fun requestGetProductList(@Query("lastId") lastId: Int): Status<ProductListResponse>

}
