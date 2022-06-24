package com.gondev.statemanager.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gondev.statemanager.state.Status

@Composable
fun <T> StateLayer(
    status: Status<T>,
    loading: @Composable () -> Unit = { DefaultLoading() },
    cachedLoading: @Composable () -> Unit = {
        DefaultCachedLoading()
    },
    error: @Composable (T?, Throwable) -> Unit = { data: T?, throwable ->
        DefaultError(data = data, throwable = throwable)
    },
    success: @Composable (T?) -> Unit,
) = when (status) {
    is Status.Error -> error(status.data, status.throwable)
    is Status.Loading ->
        if (status.data == null) {
            loading()
        } else {
            success(status.data)
            cachedLoading()
        }
    is Status.Success -> success(status.data)
}

@Composable
fun DefaultLoading() {
    Box(Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun DefaultCachedLoading() {
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun <T> DefaultError(data: T, throwable: Throwable) {

}