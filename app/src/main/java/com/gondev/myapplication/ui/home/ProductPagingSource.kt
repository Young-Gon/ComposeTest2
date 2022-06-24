package com.gondev.myapplication.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gondev.myapplication.model.api.ProductsAPI
import com.gondev.myapplication.model.data.ProductResponse

class ProductPagingSource(
    private val api: ProductsAPI,
) : PagingSource<Int, ProductResponse>() {
    override fun getRefreshKey(state: PagingState<Int, ProductResponse>) =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, ProductResponse> {
        return try {
            val response = params.key?.let { api.requestGetProductList(it) }
                ?: api.requestGetFirstProductList()
            LoadResult.Page(
                data = response.data?.products ?: emptyList(),
                prevKey = null, // Only paging forward.
                nextKey = response.data?.products?.last()?.id
            )
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            LoadResult.Error(e)
        }
    }
}