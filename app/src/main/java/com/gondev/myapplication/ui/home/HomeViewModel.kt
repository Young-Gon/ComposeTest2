package com.gondev.myapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gondev.myapplication.model.api.ProductsAPI
import com.gondev.statemanager.state.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: ProductsAPI,
) : ViewModel(){

    val productList = flow {  emit(api.requestGetFirstProductList()) }.stateIn(viewModelScope,
        SharingStarted.Lazily, Status.loading(null))
}