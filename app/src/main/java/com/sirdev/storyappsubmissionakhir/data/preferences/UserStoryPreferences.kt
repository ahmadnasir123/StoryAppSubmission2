package com.sirdev.storyappsubmissionakhir.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserStoryPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        @Volatile
        private var Sample: UserStoryPreferences? = null
        private var UserToken = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>) : UserStoryPreferences {
            return Sample ?: synchronized(this) {
                val instance = UserStoryPreferences(dataStore)
                Sample = instance
                instance
            }
        }
    }

    fun getTokenKey(): Flow<String> {
        return  dataStore.data.map { preference ->
            preference[UserToken] ?: ""
        }
    }

    suspend fun saveTokenKey(tokenUser: String) {
        dataStore.edit { preference ->
            preference[UserToken] = tokenUser
        }
    }
}