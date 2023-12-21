package com.dicoding.kenari.data.di

import android.content.Context
import com.dicoding.kenari.data.MBTIRepository
import com.dicoding.kenari.data.UserRepository
import com.dicoding.kenari.data.pref.MBTIPreference
import com.dicoding.kenari.data.pref.UserPreference
import com.dicoding.kenari.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideMBTIRepository(context: Context): MBTIRepository {
        val pref = MBTIPreference.getInstance(context.dataStore)
        return MBTIRepository.getInstance(pref)
    }
}