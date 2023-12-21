package com.dicoding.kenari.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kenari.data.MBTIRepository
import com.dicoding.kenari.data.UserRepository
import com.dicoding.kenari.data.di.Injection
import com.dicoding.kenari.view.chatbot.ChatbotViewModel
import com.dicoding.kenari.view.discussion.DiscussionViewModel
import com.dicoding.kenari.view.login.LoginViewModel
import com.dicoding.kenari.view.main.MainViewModel
import com.dicoding.kenari.view.mbti.MbtiViewModel
import com.dicoding.kenari.view.signup.SignupViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val mbtiRepository: MBTIRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ChatbotViewModel::class.java) -> {
                ChatbotViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(DiscussionViewModel::class.java) -> {
                DiscussionViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MbtiViewModel::class.java) -> {
                MbtiViewModel(userRepository, mbtiRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                val userRepository = Injection.provideRepository(context)
                val mbtiRepository = Injection.provideMBTIRepository(context)
                INSTANCE = ViewModelFactory(userRepository, mbtiRepository)
                INSTANCE!!
            }
        }
    }
}