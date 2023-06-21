package com.ilham.submissionstory.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ilham.submissionstory.StoryInjection
import com.ilham.submissionstory.model.UserPreference
import com.ilham.submissionstory.view.addstory.AddStoryViewModel
import com.ilham.submissionstory.view.login.LoginViewModel
import com.ilham.submissionstory.view.main.MainViewModel
import com.ilham.submissionstory.view.maps.MapsViewModel
import com.ilham.submissionstory.view.signup.SignUpViewModel

class ViewModelFactory(private val pref: UserPreference, private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref, StoryInjection.provider(context)) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(StoryInjection.provider(context)) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref, StoryInjection.provider(context)) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(StoryInjection.provider(context)) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(StoryInjection.provider(context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}