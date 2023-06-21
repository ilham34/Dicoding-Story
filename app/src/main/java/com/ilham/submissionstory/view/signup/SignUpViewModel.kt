package com.ilham.submissionstory.view.signup

import androidx.lifecycle.ViewModel
import com.ilham.submissionstory.model.StoryRepository

class SignUpViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun signUpAccount(name: String, email: String, password: String) = storyRepository.signUpAccount(name, email, password)
}