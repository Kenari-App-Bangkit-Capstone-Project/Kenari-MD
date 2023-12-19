package com.dicoding.kenari.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kenari.data.UserRepository
import com.dicoding.kenari.data.pref.UserModel
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}