package com.gondev.myapplication.ui.profile

import androidx.lifecycle.ViewModel
import com.gondev.myapplication.model.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
):ViewModel() {
    val token = tokenRepository.token
}