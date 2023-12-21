package com.dicoding.kenari.view.mbti

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.kenari.data.MBTIRepository
import com.dicoding.kenari.data.UserRepository
import com.dicoding.kenari.data.pref.UserModel
import kotlinx.coroutines.launch

class MbtiViewModel(
    private val userRepository: UserRepository,
    private val mbtiRepository: MBTIRepository
) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun getSelectedMBTI(): LiveData<Set<String>> {
        return mbtiRepository.getSelectedMBTI().asLiveData()
    }

    fun addMBTI(mbtiCode: String) {
        viewModelScope.launch {
            mbtiRepository.addMBTI(mbtiCode)
        }
    }

    fun resetMBTI() {
        viewModelScope.launch {
            mbtiRepository.resetMBTI()
        }
    }
}