package com.dicoding.kenari.data

import com.dicoding.kenari.data.pref.MBTIPreference
import kotlinx.coroutines.flow.Flow

class MBTIRepository private constructor(
    private val mbtiPreference: MBTIPreference
) {

    suspend fun addMBTI(mbtiCode: String) {
        mbtiPreference.addMBTI(mbtiCode)
    }

    fun getSelectedMBTI(): Flow<Set<String>> {
        return mbtiPreference.getSelectedMBTI()
    }

    suspend fun resetMBTI() {
        mbtiPreference.resetMBTI()
    }

    companion object {
        @Volatile
        private var instance: MBTIRepository? = null
        fun getInstance(
            mbtiPreference: MBTIPreference
        ): MBTIRepository =
            instance ?: synchronized(this) {
                instance ?: MBTIRepository(mbtiPreference)
            }.also { instance = it }
    }
}