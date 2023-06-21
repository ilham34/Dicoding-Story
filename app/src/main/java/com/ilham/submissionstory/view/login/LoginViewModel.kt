package com.ilham.submissionstory.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilham.submissionstory.model.StoryRepository
import com.ilham.submissionstory.model.UserModel
import com.ilham.submissionstory.model.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(
    private val pref: UserPreference,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun loginAccount(email: String, password: String) =
        storyRepository.loginAccount(email, password)
}