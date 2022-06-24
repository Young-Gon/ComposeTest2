package com.gondev.myapplication.model.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val TOKEN = stringPreferencesKey("token")

    val token = dataStore.data.map { pref ->
        pref[TOKEN]
    }

    suspend fun saveToken(token: String?) {
        dataStore.edit { pref ->
            if (token == null) {
                pref.minusAssign(TOKEN)
            } else {
                pref[TOKEN] = token
            }
        }
    }
}