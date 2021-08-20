package com.doanducdat.shoppingapp.utils

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MyDataStore @Inject constructor(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_prefs")
    private val mDataStore: DataStore<Preferences> = context.dataStore

    companion object {
        val USER_JWT = stringPreferencesKey("USER_JWT")
        val FIRST_TIME_OPEN_APP = booleanPreferencesKey("FIRST_TIME_OPEN_APP")
    }

    //read data
    val userJwtFlow: Flow<String> = mDataStore.data.map {
        it[USER_JWT] ?: ""
    }
    val firstTimeOpenAppFlow: Flow<Boolean> = mDataStore.data.map {
        it[FIRST_TIME_OPEN_APP] ?: true
    }

    //write data
    suspend fun writeJwt(userJwt: String) {
        mDataStore.edit {
            it[USER_JWT] = userJwt
        }
    }

    suspend fun writeCheckFirstTimeOpenApp(firstTimeOpenApp: Boolean) {
        Log.d("TAG", "writeCheckFirstTimeOpenApp: do it")
        mDataStore.edit {
            it[FIRST_TIME_OPEN_APP] = firstTimeOpenApp
        }
    }
}