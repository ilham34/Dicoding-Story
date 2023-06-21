package com.ilham.submissionstory.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilham.submissionstory.model.StoryRepository
import com.ilham.submissionstory.model.UserModel
import com.ilham.submissionstory.model.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val pref: UserPreference,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getUser(): Flow<UserModel> {
        return pref.getUser()
    }

    fun getStories(token: String) = storyRepository.getStories(token)

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}