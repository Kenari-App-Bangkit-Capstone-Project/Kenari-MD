package com.dicoding.kenari.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.mbtiDataStore: DataStore<Preferences> by preferencesDataStore(name = "mbti_preferences")

class MBTIPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun addMBTI(mbtiCode: String) {
        dataStore.edit { preferences ->
            val currentMBTI = preferences[MBTI_KEY] ?: emptySet()
            preferences[MBTI_KEY] = currentMBTI + mbtiCode
        }
    }

    fun getSelectedMBTI(): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[MBTI_KEY] ?: emptySet()
        }
    }

    suspend fun resetMBTI() {
        dataStore.edit { preferences ->
            preferences[MBTI_KEY] = emptySet()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MBTIPreference? = null
        private val MBTI_KEY = stringSetPreferencesKey("selected_mbti")

        fun getInstance(dataStore: DataStore<Preferences>): MBTIPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = MBTIPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}